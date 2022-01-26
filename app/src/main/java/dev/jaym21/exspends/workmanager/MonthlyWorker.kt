package dev.jaym21.exspends.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.data.models.MonthlyExpense
import dev.jaym21.exspends.data.repository.ExpenseRepository
import dev.jaym21.exspends.data.repository.MonthlyExpenseRepository
import dev.jaym21.exspends.ui.MainActivity
import dev.jaym21.exspends.utils.Constants
import dev.jaym21.exspends.utils.DateConverterUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

@HiltWorker
class MonthlyWorker @AssistedInject constructor(private val expenseRepo: ExpenseRepository, private val monthlyRepo: MonthlyExpenseRepository, @Assisted context: Context, @Assisted params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        runBlocking {
            expenseRepo.getAllExpenses().first { expenses ->
                updateMonthlyExpenses(expenses)
                true
            }
        }
        enqueueNextWorker()
        return Result.success()
    }

    private fun enqueueNextWorker() {
        val delay = DateConverterUtils.getFirstDayOfNextMonthTimestamp() - System.currentTimeMillis()
        if (delay > 0) {
            val monthWork = OneTimeWorkRequest.Builder(MonthlyWorker::class.java)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(monthWork)
        }
    }

    private fun updateMonthlyExpenses(expenses: List<Expense>) {
        runBlocking {
            var totalExpenses = 0.0
            expenses.forEach {
                totalExpenses += it.amount
            }

            val currentMonthExpense = MonthlyExpense(
                DateConverterUtils.getMonthFullName(DateConverterUtils.getFirstDayOfPreviousMonthTimestamp()),
                DateConverterUtils.getCurrentYear(),
                totalExpenses,
                System.currentTimeMillis()
            )
            monthlyRepo.insertMonthExpense(currentMonthExpense)
            expenseRepo.clearAllExpenses()
            showMonthlyTotalNotification(totalExpenses)
        }
    }

    private fun showMonthlyTotalNotification(totalExpense: Double) {

        val monthName = DateConverterUtils.getMonthFullName(DateConverterUtils.getFirstDayOfPreviousMonthTimestamp())

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, Constants.DEFAULT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Exspends")
            .setContentText("Your total expenditure for month of $monthName was ₹$totalExpense")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.DEFAULT_NOTIFICATION_CHANNEL_ID,
                Constants.DEFAULT_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}