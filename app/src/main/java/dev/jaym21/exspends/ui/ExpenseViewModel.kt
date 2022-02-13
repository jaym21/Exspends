package dev.jaym21.exspends.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.repository.ExpenseRepository
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.data.repository.MonthlyExpenseRepository
import dev.jaym21.exspends.stateflows.AllExpensesState
import dev.jaym21.exspends.stateflows.AllMonthlyExpensesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val repo: ExpenseRepository, private val monthlyRepo: MonthlyExpenseRepository): ViewModel() {

    private val _allExpenses = MutableStateFlow<AllExpensesState>(AllExpensesState.Loading)
    val allExpenses: StateFlow<AllExpensesState> = _allExpenses

    var totalExpenses = 0.0
    var areMonthlyExpensesPresent = false

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
                //updating are monthly expenses present
                getAllMonthlyExpenses()
                _allExpenses.value = AllExpensesState.Empty
            } else {
                //resetting the totals
                totalExpenses = 0.0

                //updating are monthly expenses present
                getAllMonthlyExpenses()

                //updating the total expenses
                updateTotalExpenses(expenses)

                _allExpenses.value = AllExpensesState.Success(expenses)
            }
        }
    }

    private fun getAllMonthlyExpenses() = viewModelScope.launch(Dispatchers.IO) {
        monthlyRepo.getAllMonthlyExpenses().collect {
            areMonthlyExpensesPresent = !it.isNullOrEmpty()
        }
    }

    private fun updateTotalExpenses(expenses: List<Expense>) {
        expenses.forEach {
            totalExpenses += it.amount
        }
    }
}