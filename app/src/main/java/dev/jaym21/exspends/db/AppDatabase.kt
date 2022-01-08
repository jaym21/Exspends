package dev.jaym21.exspends.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jaym21.exspends.db.models.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getExpenseDAO(): ExpenseDAO
}