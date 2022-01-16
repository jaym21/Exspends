package dev.jaym21.exspends.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.repository.ExpenseRepository
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

    var totalExpenses = 0.0
    var totalGroceries = 0.0
    var totalShopping = 0.0
    var totalSubscriptions = 0.0
    var totalEntertainment = 0.0
    var totalRestaurant = 0.0
    var totalTravel = 0.0
    var totalBills = 0.0
    var totalOthers = 0.0

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
                _allExpensesState.value = ExpenseState.Empty
            } else {
                updateTotalExpenses(expenses)
                updateCategoryTotal(expenses)
                _allExpensesState.value = ExpenseState.Success(expenses)
            }
        }
    }

    private fun updateTotalExpenses(expenses: List<Expense>) {
        expenses.forEach {
            totalExpenses += it.amount
        }
    }

    private fun updateCategoryTotal(expenses: List<Expense>) {
        expenses.forEach { expense ->
            when(expense.category) {
                "groceries" -> {
                    totalGroceries += expense.amount
                }
                "shopping" -> {
                    totalShopping += expense.amount
                }
                "subscription" -> {
                    totalSubscriptions += expense.amount
                }
                "entertainment" -> {
                    totalEntertainment += expense.amount
                }
                "restaurant" -> {
                    totalRestaurant += expense.amount
                }
                "travel" -> {
                    totalTravel += expense.amount
                }
                "bills" -> {
                    totalBills += expense.amount
                }
                "others" -> {
                    totalOthers += expense.amount
                }
            }
        }
    }
}