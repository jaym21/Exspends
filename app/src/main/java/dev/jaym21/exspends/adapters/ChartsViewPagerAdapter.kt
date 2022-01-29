package dev.jaym21.exspends.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.jaym21.exspends.ui.charts.CurrentMonthFragment
import dev.jaym21.exspends.ui.charts.MonthlyExspendsFragment

class ChartsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val navController: NavController): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val NUM_TABS = 2

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrentMonthFragment()
        }
        return MonthlyExspendsFragment(navController)
    }

}