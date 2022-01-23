package dev.jaym21.exspends.data.repository

import dev.jaym21.exspends.data.db.AppDatabase
import dev.jaym21.exspends.data.models.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val database: AppDatabase) {

    suspend fun insertExpense(expense: Expense) = database.getExpenseDAO().insertExpense(expense)

    suspend fun deleteExpense(expense: Expense) = database.getExpenseDAO().deleteExpense(expense)

    suspend fun updateExpense(expense: Expense) = database.getExpenseDAO().updateExpense(expense)

    fun getAllExpenses(): Flow<List<Expense>> = database.getExpenseDAO().getAllExpenses()

    fun getExpensesByCategory(category: String): Flow<List<Expense>> = database.getExpenseDAO().getExpensesByCategory(category)

    fun getExpenseById(id: Int): Flow<Expense?> = database.getExpenseDAO().getExpenseById(id)
}