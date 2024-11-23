package com.example.job_application_tracker.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentAuthenticateMainBinding
import com.example.job_application_tracker.views.AuthenticationActivity
import com.example.job_application_tracker.views.MainActivity
import com.google.firebase.auth.FirebaseAuth


class AuthenticateMainFragment : Fragment() {
    private lateinit var authFragBinding: FragmentAuthenticateMainBinding

    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleLogin: Button

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_authenticateMainFragment_to_mainActivity)
            Log.d("User", "${currentUser.email} signed IN")
        }
        else{
            Log.d("User", "No current user")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authFragBinding = FragmentAuthenticateMainBinding.inflate(layoutInflater,container,false)

        init()

        // Inflate the layout for this fragment
        return authFragBinding.root
    }

    private fun init() {
        btnSignIn = authFragBinding.btnSignIn
        btnSignUp = authFragBinding.btnSignUp
        btnGoogleLogin = authFragBinding.btnGoogleLogin

        navigateComponent()
    }

    private fun navigateComponent(){
        btnSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_authenticateMainFragment_to_signInFragment)
        }

        btnSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_authenticateMainFragment_to_signupFragment)
        }
    }


}