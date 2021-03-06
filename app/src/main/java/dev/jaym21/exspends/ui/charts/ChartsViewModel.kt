package dev.jaym21.exspends.ui.charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.data.repository.ExpenseRepository
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
class ChartsViewModel @Inject constructor(private val expenseRepo: ExpenseRepository, private val monthlyRepo: MonthlyExpenseRepository): ViewModel() {

    private val _allExpenses = MutableStateFlow<AllExpensesState>(AllExpensesState.Loading)
    val allExpenses: StateFlow<AllExpensesState> = _allExpenses
    private val _allMonthlyExpenses = MutableStateFlow<AllMonthlyExpensesState>(AllMonthlyExpensesState.Loading)
    val allMonthlyExpenses: StateFlow<AllMonthlyExpensesState> = _allMonthlyExpenses

    var totalExpenses = 0.0
    var totalGroceries = 0.0
    var totalShopping = 0.0
    var totalSubscriptions = 0.0
    var totalEntertainment = 0.0
    var totalRestaurant = 0.0
    var totalTravel = 0.0
    var totalBills = 0.0
    var totalInvestments = 0.0
    var totalOthers = 0.0

    fun getAllExpenses() = viewModelScope.launch(Dispatchers.IO) {
        expenseRepo.getAllExpenses().collect { expenses ->
            if (expenses.isNullOrEmpty()) {
                _allExpenses.value = AllExpensesState.Empty
            } else {
                //resetting the totals
                totalExpenses = 0.0
                totalGroceries = 0.0
                totalShopping = 0.0
                totalSubscriptions = 0.0
                totalEntertainment = 0.0
                totalRestaurant = 0.0
                totalTravel = 0.0
                totalBills = 0.0
                totalInvestments = 0.0
                totalOthers = 0.0

                //updating the total expense and total for categories
                updateTotalExpenses(expenses)
                updateCategoryTotal(expenses)

                _allExpenses.value = AllExpensesState.Success(expenses)
            }
        }
    }

    fun getAllMonthlyExpenses() = viewModelScope.launch(Dispatchers.IO) {
        monthlyRepo.getAllMonthlyExpenses().collect {
            if (it.isNullOrEmpty()) {
                _allMonthlyExpenses.value = AllMonthlyExpensesState.Empty
            } else {
                _allMonthlyExpenses.value = AllMonthlyExpensesState.Success(it)
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
                "investments" -> {
                    totalInvestments += expense.amount
                }
                "others" -> {
                    totalOthers += expense.amount
                }
            }
        }
    }
}