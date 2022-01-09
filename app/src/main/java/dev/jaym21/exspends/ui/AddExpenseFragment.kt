package dev.jaym21.exspends.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dev.jaym21.exspends.databinding.FragmentAddExpenseBinding
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : Fragment() {

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding: FragmentAddExpenseBinding
        get() = _binding!!
    private lateinit var navController: NavController

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

        binding.btnAddExpense.setOnClickListener {

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
            binding.etExpenseDate.setText(sdf.format(calendar.time))
        }

        val datePicker = DatePickerDialog(
            requireContext(),
            datePickerOnDateSetListener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
        ).run {
            datePicker.maxDate = dateToday.time
            show()
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}