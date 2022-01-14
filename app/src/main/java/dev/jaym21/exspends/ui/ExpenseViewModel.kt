package dev.jaym21.exspends.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.ExpenseRepository
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.stateflows.ExpenseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val repo: ExpenseRepository): ViewModel() {

    private val _allExpensesState = MutableStateFlow<ExpenseState>(ExpenseState.Loading)
    val allExpensesState: StateFlow<ExpenseState> = _allExpensesState

    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertExpense(expense)
    }

    fun removeExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteExpense(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateExpense(expense)
    }

    fun getAllTransaction() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllTransaction().collect { expenses ->
            if (expenses.isNullOrEmpty()) {
                _allExpensesState.value = ExpenseState.Empty
            } else {
                _allExpensesState.value = ExpenseState.Success(expenses)
            }
        }
    }
}