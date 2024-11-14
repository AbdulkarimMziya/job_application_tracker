package com.example.job_application_tracker.views

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.job_application_tracker.R
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.app_interfaces.FormFragment
import com.example.job_application_tracker.app_interfaces.FragmentNavigation
import com.example.job_application_tracker.databinding.ActivityMainBinding
import com.example.job_application_tracker.views.fragments.AddApplicationFragment
import com.example.job_application_tracker.views.fragments.ApplicationScreenFragment
import com.example.job_application_tracker.views.fragments.HomeScreenFragment
import com.example.job_application_tracker.views.fragments.ProfileScreenFragment


class MainActivity : AppCompatActivity(), BottomAppBarVisibility,FragmentNavigation {
    private lateinit var binding: ActivityMainBinding

    private lateinit var homeScreenFragment: HomeScreenFragment
    private lateinit var applicationScreenFragment: ApplicationScreenFragment
    private lateinit var profileScreenFragment: ProfileScreenFragment
    private lateinit var addApplicationFragment: AddApplicationFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)

        binding.fab.setOnClickListener {
            it.startAnimation(shakeAnim)
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
                    Toast.makeText(this, "Search Nav Triggered!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_toHome_applications -> {
                    val fab = findViewById(R.id.fab)

// Load the shake animation
                    val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)

// Apply shake animation to the FloatingActionButton
                    fab.startAnimation(shakeAnim)

// Delay fragment navigation until after animation completes (e.g., after 500ms)
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadFragment(applicationScreenFragment) // Navigate to applications fragment
                    }, shakeAnim.duration)// Navigate to applications fragment
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
