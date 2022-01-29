package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dev.jaym21.exspends.adapters.MonthlyExspendsRVAdapter
import dev.jaym21.exspends.data.models.MonthlyExpense
import dev.jaym21.exspends.databinding.FragmentAllMonthlyExspendsBinding
import dev.jaym21.exspends.stateflows.AllMonthlyExpensesState
import dev.jaym21.exspends.ui.charts.ChartsViewModel
import kotlinx.coroutines.flow.collect

class AllMonthlyExspendsFragment : Fragment() {

    private var _binding: FragmentAllMonthlyExspendsBinding? = null
    private val binding: FragmentAllMonthlyExspendsBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: ChartsViewModel
    private var monthlyExspendsAdapter = MonthlyExspendsRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllMonthlyExspendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }

        viewModel = ViewModelProvider(this).get(ChartsViewModel::class.java)

        viewModel.getAllMonthlyExpenses()

        setUpRecyclerView()

        val sample = ArrayList<MonthlyExpense>()
        sample.add(MonthlyExpense("December", "2021", 5674.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("January", "2022", 81034.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("February", "2022", 235.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("March", "2022", 83534.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("April", "2022", 2423.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("May", "2022", 64546.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("June", "2022", 547676.0, System.currentTimeMillis()))
        sample.add(MonthlyExpense("July", "2022", 3453453.0, System.currentTimeMillis()))
        monthlyExspendsAdapter.submitList(sample)

//        lifecycleScope.launchWhenCreated {
//            viewModel.allMonthlyExpenses.collect {
//                when(it) {
//                    is AllMonthlyExpensesState.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                        monthlyExspendsAdapter.submitList(it.monthlyExpenses)
//                    }
//                    is AllMonthlyExpensesState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//                    is AllMonthlyExpensesState.Empty -> {
//                        binding.progressBar.visibility = View.GONE
//                    }
//                }
//            }
//        }
    }

    private fun setUpRecyclerView() {
        binding.rvAllMonthlyExspends.apply {
            adapter = monthlyExspendsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}