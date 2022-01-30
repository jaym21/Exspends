package dev.jaym21.exspends.ui.charts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.MonthlyExpense
import dev.jaym21.exspends.databinding.FragmentMonthlyExspendsBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import dev.jaym21.exspends.stateflows.AllMonthlyExpensesState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MonthlyExspendsFragment(private val navController: NavController) : Fragment() {

    private var _binding: FragmentMonthlyExspendsBinding? = null
    private val binding: FragmentMonthlyExspendsBinding
        get() = _binding!!
    private lateinit var viewModel: ChartsViewModel
    private val xAxisLabel = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMonthlyExspendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ChartsViewModel::class.java)

        binding.llAllMonthlyExpenses.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_allMonthlyExspendsFragment)
        }

        viewModel.getAllMonthlyExpenses()

        lifecycleScope.launchWhenCreated {
            viewModel.allMonthlyExpenses.collect {
                when(it) {
                    is AllMonthlyExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.barChart.visibility = View.VISIBLE
                        binding.llAllMonthlyExpenses.visibility = View.VISIBLE
                        binding.tvNoMonthlyExpenseData.visibility = View.GONE
                        val monthlyExpenses = if (it.monthlyExpenses.size > 6) {
                            it.monthlyExpenses.subList(0, 6)
                        } else {
                            it.monthlyExpenses
                        }
                        setUpBarChart(monthlyExpenses)
                    }
                    is AllMonthlyExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllMonthlyExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.barChart.visibility = View.GONE
                        binding.llAllMonthlyExpenses.visibility = View.GONE
                        binding.tvNoMonthlyExpenseData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setUpBarChart(monthlyExpenses: List<MonthlyExpense>) {
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.setDrawBarShadow(false)
        binding.barChart.setDrawBorders(false)
        binding.barChart.animateY(1000)
        binding.barChart.setDrawValueAboveBar(true)
        binding.barChart.description.isEnabled = false
        binding.barChart.legend.isEnabled = false
        binding.barChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.barChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        monthlyExpenses.forEach {
            xAxisLabel.add("${it.month.substring(0,3)}'${it.year.substring(2)}")
        }

        val xAxis = binding.barChart.xAxis
        xAxis.granularity = 0f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.labelCount = xAxisLabel.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)

        val leftAxis = binding.barChart.axisLeft
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(true)
        val rightAxis = binding.barChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val entries = ArrayList<BarEntry>()

        var i = 0
        monthlyExpenses.forEach {
            entries.add(BarEntry(i.toFloat(), it.totalAmount.toFloat()))
            i++
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.purple_200)
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        binding.barChart.data = barData
        binding.barChart.invalidate()
    }
//
//    inner class MyAxisFormatter : IndexAxisValueFormatter() {
//
//        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//            Log.d("TAGYOYO", "getAxisLabel: $value")
//            val index = value.toInt()
//            return if (index < xAxisLabel.size) {
//                xAxisLabel[index]
//            } else {
//               return ""
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}