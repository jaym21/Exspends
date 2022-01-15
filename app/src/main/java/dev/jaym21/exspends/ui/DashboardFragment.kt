package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.adapters.ExpensesRVAdapter
import dev.jaym21.exspends.data.models.Expense
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
    private var totalExpenses = 0.0
    private var totalGroceries = 0.0
    private var totalShopping = 0.0
    private var totalSubscriptions = 0.0
    private var totalEntertainment = 0.0
    private var totalRestaurant = 0.0
    private var totalTravel = 0.0
    private var totalBills = 0.0
    private var totalOthers = 0.0

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
                        //updating latest expenses
                        val latestExpenses = if (it.expenses.size > 10) {
                            it.expenses.subList(0, 10)
                        } else {
                            it.expenses
                        }
                        expensesAdapter.submitList(latestExpenses)

                        //updating total expenses
                        it.expenses.forEach { expense ->
                            totalExpenses += expense.amount
                        }
                        binding.tvTotalExpenses.text = "₹$totalExpenses"

                        updateCategoryTotal(it.expenses)
                        setUpPieChart()
                    }
                    is ExpenseState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ExpenseState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.pieChart.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUpPieChart() {
        binding.pieChart.description.isEnabled = false
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.holeRadius = 58f
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.highlightValues(null)
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.bg_color))
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)


        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry((totalGroceries * 100/totalExpenses).toFloat(), "Groceries"))
        entries.add(PieEntry((totalShopping * 100/totalExpenses).toFloat(), "Shopping"))
        entries.add(PieEntry((totalSubscriptions * 100/totalExpenses).toFloat(), "Subscriptions"))
        entries.add(PieEntry((totalEntertainment * 100/totalExpenses).toFloat(), "Entertainment"))
        entries.add(PieEntry((totalRestaurant * 100/totalExpenses).toFloat(), "Restaurant"))
        entries.add(PieEntry((totalTravel * 100/totalExpenses).toFloat(), "Travel"))
        entries.add(PieEntry((totalBills * 100/totalExpenses).toFloat(), "Bills"))
        entries.add(PieEntry((totalOthers * 100/totalExpenses).toFloat(), "Others"))

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color.green))
        colors.add(ContextCompat.getColor(requireContext(), R.color.blue))
        colors.add(ContextCompat.getColor(requireContext(), R.color.orange))
        colors.add(ContextCompat.getColor(requireContext(), R.color.red))
        colors.add(ContextCompat.getColor(requireContext(), R.color.purple))
        colors.add(ContextCompat.getColor(requireContext(), R.color.yellow))
        colors.add(ContextCompat.getColor(requireContext(), R.color.turquoise))
        colors.add(ContextCompat.getColor(requireContext(), R.color.grey))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        binding.pieChart.data = data
        binding.pieChart.invalidate()
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