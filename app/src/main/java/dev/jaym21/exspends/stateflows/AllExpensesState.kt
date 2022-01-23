package dev.jaym21.exspends.stateflows


import dev.jaym21.exspends.data.models.Expense

sealed class AllExpensesState {
    data class Success(val expenses: List<Expense>): AllExpensesState()
    object Loading: AllExpensesState()
    object Empty: AllExpensesState()
}