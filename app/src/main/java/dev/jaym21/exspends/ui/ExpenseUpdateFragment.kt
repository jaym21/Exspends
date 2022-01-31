package dev.jaym21.exspends.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.exspends.R
import dev.jaym21.exspends.data.models.Expense
import dev.jaym21.exspends.databinding.FragmentExpenseUpdateBinding
import dev.jaym21.exspends.utils.DateConverterUtils
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExpenseUpdateFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentExpenseUpdateBinding? = null
    private val binding: FragmentExpenseUpdateBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: ExpenseViewModel
    private var expense: Expense? = null
    private var categorySelected: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        expense = arguments?.getSerializable("expense") as Expense

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }

        if (expense != null) {

            initializeOnClick()

            binding.etExpenseDateUpdate.setOnClickListener {
                openDatePicker()
            }

            updateCategoryUnderlineAndIcon(expense!!.category)
            categorySelected = expense!!.category
            binding.tvCategoryName.text = "${expense!!.category[0].uppercase()}${expense!!.category.substring(1)}"
            binding.etExpenseTitleUpdate.setText(expense!!.title)
            binding.etExpenseAmountUpdate.setText("${expense!!.amount}")
            binding.etExpenseDateUpdate.setText(expense!!.date)

            binding.btnUpdateExpense.setOnClickListener {
                if (checkAllFieldsEntered()) {
                    val updatedExpense = Expense(binding.etExpenseTitleUpdate.text.toString(), binding.etExpenseAmountUpdate.text.toString().toDouble(), categorySelected!!, binding.etExpenseDateUpdate.text.toString(),  DateConverterUtils.getTimestamp(binding.etExpenseDateUpdate.text.toString()), expense!!.id)
                    viewModel.updateExpense(updatedExpense)
                    navController.popBackStack(R.id.dashboardFragment, false)
                }
            }
        } else {
            navController.popBackStack()
        }

    }

    private fun updateCategoryUnderlineAndIcon(category:  String) {
        when(category) {
            "groceries" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.groceries_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_groceries).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                //selecting category
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_selected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "shopping" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.shopping_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_shopping).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_selected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "subscription" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.subscriptions_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_subscriptions).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_selected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "entertainment" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.entertainment_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_entertainment).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_selected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "restaurant" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.restaurant_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_restaurant).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_selected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "travel" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.travel_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_travel).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_selected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "bills" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.bills_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_bill).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.turquoise))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_selected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "investments" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.investments_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_investments).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pink))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_selected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            "others" -> {
                binding.underline.background = ContextCompat.getDrawable(requireContext(), R.drawable.others_underline_bg)
                Glide.with(requireContext()).load(R.drawable.ic_others).into(binding.ivCategoryIcon)
                binding.ivCategoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))

                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_selected_bg)
            }
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val dateToday = Date()

        val datePickerOnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            binding.etExpenseDateUpdate.setText(sdf.format(calendar.time))
        }

        DatePickerDialog(
            requireContext(),
            datePickerOnDateSetListener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
        ).run {
            datePicker.minDate = DateConverterUtils.getFirstDayOfMonthTimestamp()
            datePicker.maxDate = dateToday.time
            show()
        }
    }

    //adding category onClick listeners
    private fun initializeOnClick() {
        binding.tvGroceries.setOnClickListener(this)
        binding.tvShopping.setOnClickListener(this)
        binding.tvSubscription.setOnClickListener(this)
        binding.tvEntertainment.setOnClickListener(this)
        binding.tvRestaurant.setOnClickListener(this)
        binding.tvTravel.setOnClickListener(this)
        binding.tvBills.setOnClickListener(this)
        binding.tvInvestments.setOnClickListener(this)
        binding.tvOthers.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.tvGroceries -> {
                categorySelected = "groceries"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_selected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvShopping -> {
                categorySelected = "shopping"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_selected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvSubscription -> {
                categorySelected = "subscription"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_selected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvEntertainment -> {
                categorySelected = "entertainment"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_selected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvRestaurant -> {
                categorySelected = "restaurant"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_selected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvTravel -> {
                categorySelected = "travel"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_selected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvBills -> {
                categorySelected = "bills"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_selected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvInvestments -> {
                categorySelected = "investments"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_selected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_notselected_bg)
            }
            R.id.tvOthers -> {
                categorySelected = "others"
                binding.tvGroceries.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_groceries_notselected_bg)
                binding.tvShopping.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_shopping_notselected_bg)
                binding.tvSubscription.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_subscription_notselected_bg)
                binding.tvEntertainment.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_entertainment_notselected_bg)
                binding.tvRestaurant.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_restaurant_notselected_bg)
                binding.tvTravel.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_travel_notselected_bg)
                binding.tvBills.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_bills_notselected_bg)
                binding.tvInvestments.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_investments_notselected_bg)
                binding.tvOthers.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_others_selected_bg)
            }
        }
    }

    private fun checkAllFieldsEntered(): Boolean {
        if (binding.etExpenseTitleUpdate.text.isNotEmpty() && binding.etExpenseTitleUpdate.text.isNotBlank()) {
            if (binding.etExpenseAmountUpdate.text.isNotEmpty() && binding.etExpenseAmountUpdate.text.isNotBlank()) {
                if (binding.etExpenseDateUpdate.text.isNotEmpty() && binding.etExpenseDateUpdate.text.isNotBlank()) {
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