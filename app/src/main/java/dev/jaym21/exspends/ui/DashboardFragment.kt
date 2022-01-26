package dev.jaym21.exspends.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
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

        //TODO: Add barchart to compare total expenses of previous months

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
                    Log.d("TAGYOYO", "INSIDE2: $isFirstStartUp")
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
    }

    private fun observeLatestExpenses() {

        //observing the all expenses from the database
        lifecycleScope.launchWhenCreated {
            viewModel.allExpenses.collect {
                when(it) {
                    is AllExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.pieChart.visibility = View.VISIBLE
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

                        setUpPieChart()
                    }
                    is AllExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.pieChart.visibility = View.GONE
                        binding.tvLatestExpensesText.visibility = View.GONE
                        binding.llAllExpenses.visibility = View.GONE
                        binding.rvLatestExpenses.visibility = View.GONE
                        binding.tvTotalExpenses.text = "₹0"
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
        binding.pieChart.isRotationEnabled = false
        binding.pieChart.setTouchEnabled(false)
        binding.pieChart.highlightValues(null)
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setExtraOffsets(0f, 10f, 25f, 0f)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.bg_color))
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)


        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry((viewModel.totalGroceries * 100/viewModel.totalExpenses).toFloat(), "Groceries"))
        entries.add(PieEntry((viewModel.totalShopping * 100/viewModel.totalExpenses).toFloat(), "Shopping"))
        entries.add(PieEntry((viewModel.totalSubscriptions * 100/viewModel.totalExpenses).toFloat(), "Subscriptions"))
        entries.add(PieEntry((viewModel.totalEntertainment * 100/viewModel.totalExpenses).toFloat(), "Entertainment"))
        entries.add(PieEntry((viewModel.totalRestaurant * 100/viewModel.totalExpenses).toFloat(), "Restaurant"))
        entries.add(PieEntry((viewModel.totalTravel * 100/viewModel.totalExpenses).toFloat(), "Travel"))
        entries.add(PieEntry((viewModel.totalBills * 100/viewModel.totalExpenses).toFloat(), "Bills"))
        entries.add(PieEntry((viewModel.totalOthers * 100/viewModel.totalExpenses).toFloat(), "Others"))

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
        data.setValueFormatter(CustomPercentFormatter())
        data.setValueTextSize(10f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        //legend
        val legend = binding.pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 10f
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 30f
        legend.textColor = ContextCompat.getColor(requireContext(), R.color.white_alpha_70)
        legend.textSize = 12f

        binding.pieChart.data = data
        binding.pieChart.invalidate()
    }

    private fun setUpRecyclerView() {
        binding.rvLatestExpenses.apply {
            adapter = expensesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    class CustomPercentFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            if (value == 0.0f) {
                return  ""
            }
            return "${"%.2f".format(value)}%"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onExpenseClick(expense: Expense) {
        val bundle = bundleOf("expense" to expense)
        navController.navigate(R.id.action_dashboardFragment_to_expenseOpenFragment, bundle)
    }
}