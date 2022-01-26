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
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.databinding.FragmentCurrentMonthBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import dev.jaym21.exspends.ui.DashboardFragment
import dev.jaym21.exspends.ui.ExpenseViewModel
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
                        setUpPieChart()
                    }
                    is AllExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
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
        data.setValueFormatter(DashboardFragment.CustomPercentFormatter())
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}