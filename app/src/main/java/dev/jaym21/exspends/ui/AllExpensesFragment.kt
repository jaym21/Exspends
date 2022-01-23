package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.adapters.ExpensesRVAdapter
import dev.jaym21.exspends.adapters.IExpensesRVAdapter
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.databinding.FragmentAllExpensesBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AllExpensesFragment : Fragment(), IExpensesRVAdapter {

    private var _binding: FragmentAllExpensesBinding? = null
    private val binding: FragmentAllExpensesBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private var expensesAdapter = ExpensesRVAdapter(this)
    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        viewModel.getAllExpenses()

        setUpRecyclerView()

        //observing the all expenses from the database
        lifecycleScope.launchWhenCreated {
            viewModel.allExpenses.collect {
                when(it) {
                    is AllExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoExpenses.visibility = View.GONE
                        binding.rvAllExpenses.visibility = View.VISIBLE
                        expensesAdapter.submitList(it.expenses)
                    }
                    is AllExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvAllExpenses.visibility = View.GONE
                        binding.tvNoExpenses.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        animateUnderline()
    }

    private fun animateUnderline() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.underline_animation)
        binding.underline.startAnimation(animation)
    }

    private fun setUpRecyclerView() {
        binding.rvAllExpenses.apply {
            adapter = expensesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onExpenseClick(expense: Expense) {
        val bundle = bundleOf("expense" to expense)
        navController.navigate(R.id.action_allExpensesFragment_to_expenseOpenFragment, bundle)
    }
}