package com.mnkdev.uashealing23

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(val activity: AppCompatActivity, val fragments: ArrayList<Fragment>) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        // fragments[0] -> ExploreFragment
        // fragments[1] -> FavoriteFragment
        // fragments[2] -> ProfileFragment
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}