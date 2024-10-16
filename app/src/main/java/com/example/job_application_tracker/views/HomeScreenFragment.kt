package com.example.job_application_tracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentHomeScreenBinding


class HomeScreenFragment : Fragment(), BottomAppBarVisibility {
    private lateinit var homeScreenBinding: FragmentHomeScreenBinding

    // Bottom Nav fragments objects
    private lateinit var dashboardScreenFragment: DashboardScreenFragment
    private lateinit var applicationListScreenFragment: ApplicationListScreenFragment
    private lateinit var profileScreenFragment: ProfileScreenFragment
    private lateinit var editFormScreenFragment: EditFormScreenFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(layoutInflater,container,false)
        homeScreenBinding.bottomNavigation.background = null
        homeScreenBinding.bottomNavigation.menu.getItem(2).isEnabled  =  false

        dashboardScreenFragment  = DashboardScreenFragment()
        applicationListScreenFragment = ApplicationListScreenFragment()
        profileScreenFragment = ProfileScreenFragment()
        editFormScreenFragment = EditFormScreenFragment()

        // Set up initial fragment
        loadFragment(dashboardScreenFragment)

        // Set Floating Action Button onClick Listener
        homeScreenBinding.fab.setOnClickListener{
            // Create the new fragment instance
            // Slide up entry transition, Slide down exit transition
            loadFragment(editFormScreenFragment)
        }

        // Set up bottom navigation
        homeScreenBinding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_toHome_dashboard -> {
                    loadFragment(dashboardScreenFragment)
                    true
                }
                R.id.menu_search_applications -> {
                    Toast.makeText(requireContext(),"Search Nav Triggered!", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        homeScreenBinding.coordinatorLayout.isVisible = true // Show BottomAppBar when fragment resumes
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the new one
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeScreenContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun showBottomAppBar() {
        homeScreenBinding.coordinatorLayout.isVisible = true
    }

    override fun hideBottomAppBar() {
        homeScreenBinding.coordinatorLayout.isVisible = false
    }

}