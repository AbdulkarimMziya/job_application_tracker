package com.example.job_application_tracker.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.job_application_tracker.R
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.app_interfaces.FragmentNavigation
import com.example.job_application_tracker.databinding.ActivityMainBinding
import com.example.job_application_tracker.views.fragments.AddApplicationFragment
import com.example.job_application_tracker.views.fragments.ApplicationScreenFragment
import com.example.job_application_tracker.views.fragments.HomeScreenFragment
import com.example.job_application_tracker.views.fragments.ProfileScreenFragment
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), BottomAppBarVisibility, FragmentNavigation {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeScreenFragment: HomeScreenFragment
    private lateinit var applicationScreenFragment: ApplicationScreenFragment
    private lateinit var profileScreenFragment: ProfileScreenFragment
    private lateinit var addApplicationFragment: AddApplicationFragment

    private val authListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        if (user != null) {
            // User is signed in
            // Optionally, you can update the UI or perform actions based on user data here
        } else {
            // User is signed out, navigate to authentication flow
            navigateToAuthenticationScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        // Listen for the floating action button click
        binding.fab.setOnClickListener {
            val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)
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

        // Load the initial fragment based on authentication status
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // If the user is already logged in, show the home screen
            loadFragment(homeScreenFragment)
        } else {
            // Otherwise, show the authentication screen (handle in onAuthStateChanged)
            navigateToAuthenticationScreen()
        }

        // Set up BottomNavigationView for navigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_toHome_dashboard -> {
                    loadFragment(homeScreenFragment)
                    true
                }
                R.id.menu_search_applications -> {
                    loadFragment(applicationScreenFragment)
                    applicationScreenFragment.focusSearchView()
                    true
                }
                R.id.menu_toHome_applications -> {
                    // Load the shake animation
                    val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)

                    // Apply shake animation to the FloatingActionButton
                    binding.fab.startAnimation(shakeAnim)

                    // Delay fragment navigation until after animation completes (e.g., after 500ms)
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadFragment(applicationScreenFragment) // Navigate to applications fragment
                    }, shakeAnim.duration)
                    true
                }
                R.id.menu_toHome_profile -> {
                    loadFragment(profileScreenFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToAuthenticationScreen() {
        // If the user is not logged in, navigate to the authentication screen
        val intent = Intent(this, AuthenticationActivity::class.java)  // Assuming AuthenticationActivity is your login/signup activity
        startActivity(intent)
        finish()  // Close the MainActivity to prevent the user from returning back
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
