package dev.jaym21.exspends.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.adapters.ExpensesRVAdapter
import dev.jaym21.exspends.databinding.FragmentDashboardBinding
import dev.jaym21.exspends.stateflows.ExpenseState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private var expensesAdapter = ExpensesRVAdapter()
    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        binding.fabAddExpense.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
        }

        viewModel.getAllTransaction()

        setUpRecyclerView()

        //observing the all expenses from the database
        lifecycleScope.launchWhenCreated {
            viewModel.allExpensesState.collect {
                when(it) {
                    is ExpenseState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val expenses = if (it.expenses.size > 10) {
                            it.expenses.subList(0, 10)
                        } else {
                            it.expenses
                        }
                        expensesAdapter.submitList(expenses)
                    }
                    is ExpenseState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ExpenseState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvLatestExpenses.apply {
            adapter = expensesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}