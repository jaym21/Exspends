package dev.jaym21.exspends.db.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpense(expense: Expense)

    @Query("SELECT * FROM expenses_table ORDER by createdAt DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM EXPENSES_TABLE WHERE category = :category ORDER by createdAt DESC")
    fun getExpensesByCategory(category: String): Flow<List<Expense>>
}