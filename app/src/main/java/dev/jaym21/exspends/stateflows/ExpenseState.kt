package dev.jaym21.exspends.stateflows

import dev.jaym21.exspends.data.models.Expense

sealed class ExpenseState {
    data class Success(val expense: Expense): ExpenseState()
    object Loading: ExpenseState()
    object Empty: ExpenseState()
}