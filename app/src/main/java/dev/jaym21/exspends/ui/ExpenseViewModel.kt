package dev.jaym21.exspends.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.repository.ExpenseRepository
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.stateflows.AllExpensesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val repo: ExpenseRepository): ViewModel() {

    private val _allExpenses = MutableStateFlow<AllExpensesState>(AllExpensesState.Loading)
    val allExpenses: StateFlow<AllExpensesState> = _allExpenses

    var totalExpenses = 0.0

    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertExpense(expense)
    }

    fun removeExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteExpense(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateExpense(expense)
    }

    fun getAllExpenses() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllExpenses().collect { expenses ->
            if (expenses.isNullOrEmpty()) {
                _allExpenses.value = AllExpensesState.Empty
            } else {
                //resetting the totals
                totalExpenses = 0.0

                //updating the total expenses
                updateTotalExpenses(expenses)

                _allExpenses.value = AllExpensesState.Success(expenses)
            }
        }
    }

    private fun updateTotalExpenses(expenses: List<Expense>) {
        expenses.forEach {
            totalExpenses += it.amount
        }
    }
}