package dev.jaym21.exspends.ui.charts

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.databinding.FragmentCurrentMonthBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CurrentMonthFragment : Fragment() {

    private var _binding: FragmentCurrentMonthBinding? = null
    private val binding: FragmentCurrentMonthBinding
        get() = _binding!!
    private lateinit var viewModel: ChartsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ChartsViewModel::class.java)

        viewModel.getAllExpenses()

        lifecycleScope.launchWhenCreated {
            viewModel.allExpenses.collect {
                when(it) {
                    is AllExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvCurrentMonthData.visibility = View.GONE
                        binding.pieChart.visibility = View.VISIBLE
                        setUpPieChart()
                    }
                    is AllExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvCurrentMonthData.visibility = View.VISIBLE
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
        binding.pieChart.isRotationEnabled = false
        binding.pieChart.setTouchEnabled(false)
        binding.pieChart.highlightValues(null)
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setExtraOffsets(10f, 10f, 55f, 10f)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.bg_color))
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)


        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        val groceriesValue = (viewModel.totalGroceries * 100/viewModel.totalExpenses).toFloat()
        val shoppingValue = (viewModel.totalShopping * 100/viewModel.totalExpenses).toFloat()
        val subscriptionsValue = (viewModel.totalSubscriptions * 100/viewModel.totalExpenses).toFloat()
        val entertainmentValue = (viewModel.totalEntertainment * 100/viewModel.totalExpenses).toFloat()
        val restaurantValue = (viewModel.totalRestaurant * 100/viewModel.totalExpenses).toFloat()
        val travelValue = (viewModel.totalTravel * 100/viewModel.totalExpenses).toFloat()
        val billsValue = (viewModel.totalBills * 100/viewModel.totalExpenses).toFloat()
        val investmentsValue = (viewModel.totalInvestments * 100/viewModel.totalExpenses).toFloat()
        val othersValue = (viewModel.totalOthers * 100/viewModel.totalExpenses).toFloat()


        if (groceriesValue > 0.0f) {
            entries.add(PieEntry(groceriesValue,"Groceries"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (shoppingValue > 0.0f) {
            entries.add(PieEntry(shoppingValue,"Shopping"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue))
        }
        if (subscriptionsValue > 0.0f) {
            entries.add(PieEntry(subscriptionsValue, "Subscriptions"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange))
        }
        if (entertainmentValue > 0.0f) {
            entries.add(PieEntry(entertainmentValue, "Entertainment"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.red))
        }
        if (restaurantValue > 0.0f) {
            entries.add(PieEntry(restaurantValue, "Restaurant"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.purple))
        }
        if (travelValue > 0.0f) {
            entries.add(PieEntry(travelValue, "Travel"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.yellow))
        }
        if (billsValue > 0.0f) {
            entries.add(PieEntry(billsValue, "Bills"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.turquoise))
        }
        if (investmentsValue > 0.0f) {
            entries.add(PieEntry(investmentsValue, "Investments"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.pink))
        }
        if (othersValue > 0.0f) {
            entries.add(PieEntry(othersValue, "Others"))
            colors.add(ContextCompat.getColor(requireContext(), R.color.grey))
        }


        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.isUsingSliceColorAsValueLineColor = true
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 100f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.2f

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
        legend.yOffset = 10f
        legend.textColor = ContextCompat.getColor(requireContext(), R.color.white_alpha_70)
        legend.textSize = 12f

        binding.pieChart.data = data
        binding.pieChart.invalidate()
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
}