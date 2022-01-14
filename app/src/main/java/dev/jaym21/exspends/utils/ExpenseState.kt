package dev.jaym21.exspends.utils


import dev.jaym21.exspends.data.models.Expense

sealed class ExpenseState {
    data class Success(val expenses: List<Expense>): ExpenseState()
    data class Error(val message: String): ExpenseState()
    object Loading: ExpenseState()
    object Empty: ExpenseState()
}