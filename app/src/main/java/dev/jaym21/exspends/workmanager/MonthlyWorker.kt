package dev.jaym21.exspends.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.data.models.MonthlyExpense
import dev.jaym21.exspends.data.repository.ExpenseRepository
import dev.jaym21.exspends.data.repository.MonthlyExpenseRepository
import dev.jaym21.exspends.utils.DateConverterUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

@HiltWorker
class MonthlyWorker @AssistedInject constructor(private val expenseRepo: ExpenseRepository, private val monthlyRepo: MonthlyExpenseRepository, @Assisted context: Context, @Assisted params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
            runBlocking {
                expenseRepo.getAllExpenses().collect { expenses ->
                    updateMonthlyExpenses(expenses)
                }
            }
            return Result.success()
        }


    private fun updateMonthlyExpenses(expenses: List<Expense>) {
        runBlocking {
            var totalExpenses = 0.0
            expenses.forEach {
                totalExpenses += it.amount
            }

            val currentMonthExpense = MonthlyExpense(
                DateConverterUtils.getCurrentMonthFullName(),
                DateConverterUtils.getCurrentYear(),
                totalExpenses,
                System.currentTimeMillis()
            )
            monthlyRepo.insertMonthExpense(currentMonthExpense)
            expenseRepo.clearAllExpenses()
        }
    }

}