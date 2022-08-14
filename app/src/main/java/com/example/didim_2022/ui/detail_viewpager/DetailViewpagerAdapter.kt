package com.example.didim_2022.ui.detail_viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.didim_2022.ui.DetailFragment

class DetailViewpagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DayFragment()
            1 -> WeekFragment()
            2 -> MonthFragment()
            else -> DetailFragment()
        }
    }
}