package com.akproduction.job_application_tracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.akproduction.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.akproduction.job_application_tracker.databinding.FragmentHomeScreenBinding
import com.akproduction.job_application_tracker.viewmodel.JobApplicationViewModel
import com.akproduction.job_application_tracker.viewmodel.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeScreenFragment : Fragment() {

    private lateinit var homeScreenBinding: FragmentHomeScreenBinding
    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    private lateinit var tvTotalApplications: TextView
    private lateinit var tvRejectedApplications: TextView
    private lateinit var tvTotalInterviews: TextView
    private lateinit var tvTotalOffers: TextView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    // Track the currently selected tab
    private var currentTabPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(layoutInflater, container, false)
        init()
        return homeScreenBinding.root
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()

        // Set the currently selected tab when the fragment resumes
        tabLayout.getTabAt(currentTabPosition)?.select()
    }

    private fun init() {
        tvTotalApplications = homeScreenBinding.tvTotalApplications
        tvRejectedApplications = homeScreenBinding.tvRejectedApplications
        tvTotalInterviews = homeScreenBinding.tvTotalInterviews
        tvTotalOffers = homeScreenBinding.tvTotalOffers

        tabLayout = homeScreenBinding.tabLayout
        viewPager2 = homeScreenBinding.viewPager2
        viewPagerAdapter = ViewPagerAdapter(this)

        // Initialize the ViewModel
        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        displayBoard()

        viewPagerDisplay()
    }

    private fun viewPagerDisplay() {
        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Recent Applications"
                1 -> "Interviews"
                else -> null
            }
        }.attach()

    }

    private fun displayBoard() {
        // Observe application count
        mJobApplicationViewModel.applicationCount.observe(viewLifecycleOwner) { count ->
            tvTotalApplications.text = "$count"
        }

        // Observe rejected application count
        mJobApplicationViewModel.getCountOfRejectedApplications().observe(viewLifecycleOwner) { count ->
            tvRejectedApplications.text = "$count"
        }

        // Observe interview count
        mJobApplicationViewModel.getCountOfInterviews().observe(viewLifecycleOwner) { count ->
            tvTotalInterviews.text = "$count"
        }

        // Observe offers count
        mJobApplicationViewModel.getCountOfOffers().observe(viewLifecycleOwner) { count ->
            tvTotalOffers.text = "$count"
        }
    }
}
