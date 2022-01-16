package dev.jaym21.exspends.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dev.jaym21.exspends.R
import dev.jaym21.exspends.databinding.FragmentAllExpensesBinding

class AllExpensesFragment : Fragment() {

    private var _binding: FragmentAllExpensesBinding? = null
    private val binding: FragmentAllExpensesBinding
        get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}