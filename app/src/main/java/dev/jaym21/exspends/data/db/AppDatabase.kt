package dev.jaym21.exspends.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.data.models.MonthlyExpense

@Database(entities = [Expense::class, MonthlyExpense::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getExpenseDAO(): ExpenseDAO

    abstract fun getMonthlyExpenseDAO(): MonthlyExpenseDAO
}