package dev.jaym21.exspends.data.repository

import dev.jaym21.exspends.data.db.AppDatabase
import dev.jaym21.exspends.data.models.MonthlyExpense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MonthlyExpenseRepository @Inject constructor(private val database: AppDatabase) {

    suspend fun insertMonthExpense(monthlyExpense: MonthlyExpense) = database.getMonthlyExpenseDAO().insertMonthExpense(monthlyExpense)

    fun getAllMonthlyExpenses(): Flow<List<MonthlyExpense>> = database.getMonthlyExpenseDAO().getAllMonthlyExpenses()
}