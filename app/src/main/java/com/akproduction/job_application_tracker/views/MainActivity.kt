package com.akproduction.job_application_tracker.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.akproduction.job_application_tracker.R
import com.akproduction.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.akproduction.job_application_tracker.app_interfaces.FragmentNavigation
import com.akproduction.job_application_tracker.databinding.ActivityMainBinding
import com.akproduction.job_application_tracker.views.fragments.AddApplicationFragment
import com.akproduction.job_application_tracker.views.fragments.ApplicationScreenFragment
import com.akproduction.job_application_tracker.views.fragments.HomeScreenFragment
import com.akproduction.job_application_tracker.views.fragments.ProfileScreenFragment


class MainActivity : AppCompatActivity(), BottomAppBarVisibility,FragmentNavigation {
    private lateinit var binding: ActivityMainBinding

    private lateinit var homeScreenFragment: HomeScreenFragment
    private lateinit var applicationScreenFragment: ApplicationScreenFragment
    private lateinit var profileScreenFragment: ProfileScreenFragment
    private lateinit var addApplicationFragment: AddApplicationFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.fab.setOnClickListener {
            hideBottomAppBar()
            loadFragment(addApplicationFragment)
        }
    }

    private fun init() {
        // Set up the bottom navigation
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(2).isEnabled = false

        // Initialize fragments
        homeScreenFragment = HomeScreenFragment()
        applicationScreenFragment = ApplicationScreenFragment()
        profileScreenFragment = ProfileScreenFragment()
        addApplicationFragment = AddApplicationFragment()

        // Load the initial fragment
        loadFragment(homeScreenFragment)

        // Set up BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_toHome_dashboard -> {
                    loadFragment(homeScreenFragment) // Navigate to the dashboard fragment
                    true
                }
                R.id.menu_search_applications -> {
                    loadFragment(applicationScreenFragment)
                    applicationScreenFragment.focusSearchView()
                    true
                }
                R.id.menu_toHome_applications -> {
                    loadFragment(applicationScreenFragment) // Navigate to applications fragment
                    true
                }
                R.id.menu_toHome_profile -> {
                    loadFragment(profileScreenFragment) // Navigate to profile fragment
                    true
                }
                else -> false
            }
        }

    }



    override fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the new one
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun showBottomAppBar() {
        binding.coordinatorLayout.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun hideBottomAppBar() {
        binding.coordinatorLayout.visibility = View.GONE
        binding.bottomNavigation.visibility = View.GONE
    }
}