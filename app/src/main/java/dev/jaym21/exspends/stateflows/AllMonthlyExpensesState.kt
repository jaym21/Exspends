package dev.jaym21.exspends.stateflows


import dev.jaym21.exspends.data.models.MonthlyExpense

sealed class AllMonthlyExpensesState {
    data class Success(val expenses: List<MonthlyExpense>): AllMonthlyExpensesState()
    object Loading: AllMonthlyExpensesState()
    object Empty: AllMonthlyExpensesState()
}