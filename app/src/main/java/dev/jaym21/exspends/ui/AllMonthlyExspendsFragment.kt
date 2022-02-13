package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.adapters.MonthlyExspendsRVAdapter
import dev.jaym21.exspends.databinding.FragmentAllMonthlyExspendsBinding
import dev.jaym21.exspends.stateflows.AllMonthlyExpensesState
import dev.jaym21.exspends.ui.charts.ChartsViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
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

        lifecycleScope.launchWhenCreated {
            viewModel.allMonthlyExpenses.collect {
                when(it) {
                    is AllMonthlyExpensesState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        monthlyExspendsAdapter.submitList(it.monthlyExpenses.reversed())
                    }
                    is AllMonthlyExpensesState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AllMonthlyExpensesState.Empty -> {
                        binding.progressBar.visibility = View.GONE
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