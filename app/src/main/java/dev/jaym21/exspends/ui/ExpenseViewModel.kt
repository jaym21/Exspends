package dev.jaym21.exspends.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.ExpenseRepository
import dev.jaym21.exspends.data.models.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val repo: ExpenseRepository): ViewModel() {

    private val allExpensesState: MutableStateFlow<> = MutableStateFlow()

    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertExpense(expense)
    }

    fun removeExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteExpense(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateExpense(expense)
    }


}