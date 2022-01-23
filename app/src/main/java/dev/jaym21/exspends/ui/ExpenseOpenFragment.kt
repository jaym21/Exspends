package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.databinding.FragmentExpenseOpenBinding
import dev.jaym21.exspends.stateflows.AllExpensesState
import dev.jaym21.exspends.stateflows.ExpenseState
import dev.jaym21.exspends.utils.DateConverterUtils
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExpenseOpenFragment : Fragment() {

    private var _binding: FragmentExpenseOpenBinding? = null
    private val binding: FragmentExpenseOpenBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: ExpenseViewModel
    private var expenseId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }

        expenseId = arguments?.getInt("id")

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        if (expenseId != null)
            viewModel.getExpenseWithId(expenseId!!)
        else
            navController.popBackStack()

        lifecycleScope.launchWhenCreated {
            viewModel.expenseById.collect {
                when (it) {
                    is ExpenseState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvCategoryName.text = "${it.expense.category[0].uppercase()}${it.expense.category.substring(1)}"
                        updateCategoryUnderlineAndIcon(it.expense.category)
                        binding.tvExpenseTitle.text = it.expense.title
                        binding.tvExpenseAmount.text = "₹${it.expense.amount}"
                        val dateTimestamp = DateConverterUtils.getTimestamp(it.expense.date)
                        binding.tvExpenseDate.text = DateConverterUtils.convertDateFormat(dateTimestamp)
                    }
                    is ExpenseState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ExpenseState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    private fun updateCategoryUnderlineAndIcon(category:  String) {
         when(category) {
            "groceries" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.groceries_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_groceries).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            }
            "shopping" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.shopping_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_shopping).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))
            }
            "subscription" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.subscriptions_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_subscriptions).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange))
            }
            "entertainment" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.entertainment_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_entertainment).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
            }
            "restaurant" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.restaurant_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_restaurant).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple))
            }
            "travel" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.travel_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_travel).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow))
            }
            "bills" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.bills_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_bill).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.turquoise))
            }
            "others" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.others_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_others).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}