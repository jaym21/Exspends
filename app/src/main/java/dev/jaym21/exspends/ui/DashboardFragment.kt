package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.adapters.ChartsViewPagerAdapter
import dev.jaym21.exspends.adapters.ExpensesRVAdapter
import dev.jaym21.exspends.adapters.IExpensesRVAdapter
import dev.jaym21.exspends.data.datastore.DataStoreManager
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.databinding.FragmentDashboardBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import dev.jaym21.exspends.utils.DateConverterUtils
import dev.jaym21.exspends.workmanager.MonthlyWorker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DashboardFragment : Fragment(), IExpensesRVAdapter {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private var expensesAdapter = ExpensesRVAdapter(this)
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var chartsViewPagerAdapter: ChartsViewPagerAdapter
    private val charts = arrayOf("Current Month", "Monthly Expenses")

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


        runBlocking {
            DataStoreManager(requireContext()).isFirstStartUp.asLiveData().observe(viewLifecycleOwner) { isFirstStartUp ->
                if (isFirstStartUp) {
                    val delay = DateConverterUtils.getFirstDayOfNextMonthTimestamp() - System.currentTimeMillis()
                    if (delay > 0) {
                        val monthWork = OneTimeWorkRequest.Builder(MonthlyWorker::class.java)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .build()

                        WorkManager.getInstance(requireContext()).enqueue(monthWork)
                    }
                }
            }
        }
        runBlocking {
            DataStoreManager(requireContext()).isFirstStartUp.asLiveData()
                .observe(viewLifecycleOwner) { isFirstStartUp ->
                    if (isFirstStartUp) {
                        lifecycleScope.launch {
                            DataStoreManager(requireContext()).saveIsFirstStartUp(false)
                        }
                    }
                }
        }

        //initializing navController
        navController = Navigation.findNavController(view)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        binding.fabAddExpense.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
        }

        setUpRecyclerView()

        viewModel.getAllExpenses()

        //setting current month
        binding.tvCurrentMonth.text = "${DateConverterUtils.getMonthFullName(System.currentTimeMillis())}' ${DateConverterUtils.getCurrentYearShort()}"


        binding.llAllExpenses.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_allExpensesFragment)
        }

        observeLatestExpenses()

        chartsViewPagerAdapter = ChartsViewPagerAdapter(childFragmentManager, lifecycle, navController)

        binding.viewPager.adapter = chartsViewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = charts[position]
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab  = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0,0,20,0)
            tab.requestLayout()
        }
    }

    private fun observeLatestExpenses() {

        //observing the all expenses from the database
        lifecycleScope.launchWhenCreated {
            viewModel.allExpenses.collect {
                when(it) {
                    is AllExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoExpensesDataText.visibility = View.GONE
                        binding.ivCurlingArrow.visibility = View.GONE
                        binding.tabLayout.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.VISIBLE
                        binding.rvLatestExpenses.visibility = View.VISIBLE
                        binding.tvLatestExpensesText.visibility = View.VISIBLE
                        binding.llAllExpenses.visibility = View.VISIBLE
                        //updating latest expenses
                        val latestExpenses = if (it.expenses.size > 10) {
                            it.expenses.subList(0, 10)
                        } else {
                            it.expenses
                        }
                        expensesAdapter.submitList(latestExpenses)

                        binding.tvTotalExpenses.text = "₹${viewModel.totalExpenses}"

                    }
                    is AllExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvLatestExpensesText.visibility = View.GONE
                        binding.llAllExpenses.visibility = View.GONE
                        binding.rvLatestExpenses.visibility = View.GONE
                        binding.tvNoExpensesDataText.visibility = View.VISIBLE
                        binding.ivCurlingArrow.visibility = View.VISIBLE
                        binding.tvTotalExpenses.text = "₹0"
                        if (viewModel.areMonthlyExpensesPresent) {
                            binding.tabLayout.visibility = View.VISIBLE
                            binding.viewPager.visibility = View.VISIBLE
                            binding.tvNoExpensesDataText.visibility = View.GONE
                            binding.ivCurlingArrow.visibility = View.GONE
                        } else {
                            binding.tabLayout.visibility = View.GONE
                            binding.viewPager.visibility = View.GONE
                            binding.tvNoExpensesDataText.visibility = View.VISIBLE
                            binding.ivCurlingArrow.visibility = View.VISIBLE
                        }
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

    override fun onExpenseClick(expense: Expense) {
        val bundle = Bundle()
        bundle.putSerializable("expense", expense)
        navController.navigate(R.id.action_dashboardFragment_to_expenseOpenFragment, bundle)
    }
}