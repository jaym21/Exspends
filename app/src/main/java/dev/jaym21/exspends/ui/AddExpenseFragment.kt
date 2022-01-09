package dev.jaym21.exspends.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.databinding.FragmentAddExpenseBinding
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddExpenseFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding: FragmentAddExpenseBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private var categorySelected: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.etExpenseDate.setOnClickListener {
            openDatePicker()
        }

        //adding category onClick listeners
        binding.tvGroceries.setOnClickListener(this)
        binding.tvShopping.setOnClickListener(this)
        binding.tvSubscription.setOnClickListener(this)
        binding.tvEatingOut.setOnClickListener(this)
        binding.tvTravel.setOnClickListener(this)
        binding.tvOthers.setOnClickListener(this)


        binding.btnAddExpense.setOnClickListener {
            if (checkAllFieldsEntered()) {
                val newExpense = Expense(binding.etExpenseTitle.text.toString(), binding.etExpenseAmount.text.toString().toDouble(), categorySelected!!, binding.etExpenseDate.text.toString())
                expenseViewModel.addExpense(newExpense)
                navController.popBackStack()
            }
        }
    }

    private fun checkAllFieldsEntered(): Boolean {
        if (binding.etExpenseTitle.text.isNotEmpty() && binding.etExpenseTitle.text.isNotBlank()) {
            if (binding.etExpenseAmount.text.isNotEmpty() && binding.etExpenseAmount.text.isNotBlank()) {
                if (binding.etExpenseDate.text.isNotEmpty() && binding.etExpenseDate.text.isNotBlank()) {
                    if (categorySelected != null) {
                        return true
                    }else {
                        Snackbar.make(binding.root, "Expense category needs to be selected", Snackbar.LENGTH_SHORT).show()
                    }
                }else {
                    Snackbar.make(binding.root, "Expense date cannot be empty", Snackbar.LENGTH_SHORT).show()
                }
            }else {
                Snackbar.make(binding.root, "Expense amount cannot be empty", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(binding.root, "Expense title cannot be empty", Snackbar.LENGTH_SHORT).show()
        }
        return false
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val dateToday = Date()

        val datePickerOnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            binding.etExpenseDate.setText(sdf.format(calendar.time))
        }

        DatePickerDialog(
            requireContext(),
            datePickerOnDateSetListener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
        ).run {
            datePicker.maxDate = dateToday.time
            show()
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.tvGroceries -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_selected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvShopping -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_selected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvSubscription -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_selected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvEatingOut -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_selected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvTravel -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_selected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvOthers -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEatingOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_eatingout_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_selected_bg)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}