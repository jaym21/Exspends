package dev.jaym21.exspends.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.exspends.data.ExpenseRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repo: ExpenseRepository) : ViewModel() {



}