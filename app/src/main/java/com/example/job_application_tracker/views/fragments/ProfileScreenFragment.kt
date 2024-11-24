package com.example.job_application_tracker.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentProfileScreenBinding
import com.example.job_application_tracker.views.AuthenticationActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileScreenFragment : Fragment() {
    private lateinit var profileScreenBinding: FragmentProfileScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileScreenBinding = FragmentProfileScreenBinding.inflate(inflater, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Populate profile data if the user is logged in
        populateProfileData()

        // Sign Out button click listener
        profileScreenBinding.btnSignOut.setOnClickListener {
            signOutUser()
        }

        applyScaleUpAnimation(profileScreenBinding.ivFollowMe)
        // Open Abdul's link
        profileScreenBinding.btnOpenAbdulLink.setOnClickListener {
            openUrl("https://www.abdulmziya.netlify.app")
        }


        applyScaleUpAnimation(profileScreenBinding.ivFollowMe1)
        // Open Gabriel's link
        profileScreenBinding.btnOpenGabrielLink.setOnClickListener {
            openUrl("https://www.linkedin.com/in/gabriel-eremie-0ab159250/")
        }

        return profileScreenBinding.root
    }

    private fun populateProfileData() {
        val user = auth.currentUser
        if (user != null) {
            profileScreenBinding.tvEmail.text = user.email
            profileScreenBinding.tvUsername.text = user.email
        }
    }

    // Function to apply the scale-up animation from XML
    private fun applyScaleUpAnimation(imageView: View) {
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        imageView.startAnimation(scaleUp)
    }

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun signOutUser() {
        // Sign out the user
        Firebase.auth.signOut()

        // Launch AuthenticationActivity after sign out
        val intent = Intent(requireContext(), AuthenticationActivity::class.java)
        startActivity(intent)

        // Optionally finish the current activity to prevent the user from returning
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()

        // Re-fetch the current user when the fragment resumes
        populateProfileData()

        // Show bottom app bar if necessary
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()
    }
}
