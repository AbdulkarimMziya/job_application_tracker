package com.example.job_application_tracker.viewmodel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.job_application_tracker.views.fragments.NotificationViewPagerFragment
import com.example.job_application_tracker.views.fragments.RecentApplicationViewPagerFragment
import com.example.job_application_tracker.views.fragments.HomeScreenFragment

class ViewPagerAdapter(fragmentActivity: HomeScreenFragment) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: List<Fragment> = listOf(
        RecentApplicationViewPagerFragment(),
        NotificationViewPagerFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
