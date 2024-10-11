package com.example.job_application_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.job_application_tracker.databinding.FragmentHomeScreenBinding


class HomeScreenFragment : Fragment() {
    private lateinit var homeScreenBinding: FragmentHomeScreenBinding

    // Bottom Nav fragments objects
    private lateinit var dashboardScreenFragment: DashboardScreenFragment
    private lateinit var applicationListScreenFragment: ApplicationListScreenFragment
    private lateinit var profileScreenFragment: ProfileScreenFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(layoutInflater,container,false)

        dashboardScreenFragment  = DashboardScreenFragment()
        applicationListScreenFragment = ApplicationListScreenFragment()
        profileScreenFragment = ProfileScreenFragment()

        // Set up initial fragment
        loadFragment(dashboardScreenFragment)

        // Set up bottom navigation
        homeScreenBinding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_toHome_dashboard -> {
                    loadFragment(dashboardScreenFragment)
                    true
                }
                R.id.menu_toHome_applications -> {
                    loadFragment(applicationListScreenFragment)
                    true
                }
                R.id.menu_toHome_profile -> {
                    loadFragment(profileScreenFragment)
                    true
                }
                else -> false
            }

        }


        // Inflate the layout for this fragment
        return homeScreenBinding.root
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the new one
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeScreenContainer, fragment) // Assuming you have a container with this ID
        transaction.commit()
    }

}