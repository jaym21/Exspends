package dev.jaym21.exspends.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.databinding.FragmentExpenseOpenBinding
import dev.jaym21.exspends.utils.DateConverterUtils

@AndroidEntryPoint
class ExpenseOpenFragment : Fragment() {

    private var _binding: FragmentExpenseOpenBinding? = null
    private val binding: FragmentExpenseOpenBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: ExpenseViewModel
    private var expense: Expense? = null

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

        expense = arguments?.getParcelable("expense")

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        if (expense != null) {

            binding.ivDeleteExpense.setOnClickListener {
                showDeleteDialog()
            }

            binding.ivEditExpense.setOnClickListener {
                val bundle = bundleOf("expense" to expense)
                navController.navigate(R.id.action_expenseOpenFragment_to_expenseUpdateFragment, bundle)
            }

            updateCategoryUnderlineAndIcon(expense!!.category)

            binding.tvCategoryName.text = "${expense!!.category[0].uppercase()}${expense!!.category.substring(1)}"
            binding.tvExpenseTitle.text = expense!!.title
            binding.tvExpenseAmount.text = "₹${expense!!.amount}"
            val dateTimestamp = DateConverterUtils.getTimestamp(expense!!.date)
            if (dateTimestamp != null) {
                binding.tvExpenseDate.text = DateConverterUtils.convertDateFormat(dateTimestamp)
            }
        } else {
            navController.popBackStack()
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.DeleteAlertDialog).create()

        val view = layoutInflater.inflate(R.layout.delete_dialog_layout, null)
        val deleteButton: MaterialButton = view.findViewById(R.id.btnDelete)
        val cancelButton: MaterialButton = view.findViewById(R.id.btnCancel)

        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)

        deleteButton.setOnClickListener {
            if (expense != null) {
                viewModel.removeExpense(expense!!)
                builder.dismiss()
                navController.popBackStack()
            } else {
                Snackbar.make(binding.root, "Could not delete expense, try again!", Snackbar.LENGTH_SHORT).show()
                builder.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
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
             "investments" -> {
                 binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.investments_underline_bg)
                 Glide.with(requireContext()).load(R.drawable.ic_investments).into(binding.ivCategoryIcon)
                 binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pink))
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