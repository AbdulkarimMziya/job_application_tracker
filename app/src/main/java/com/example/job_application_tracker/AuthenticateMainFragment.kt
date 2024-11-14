package com.example.job_application_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.job_application_tracker.databinding.ActivityAuthenticationBinding
import com.example.job_application_tracker.databinding.FragmentAuthenticateMainBinding


class AuthenticateMainFragment : Fragment() {
    private lateinit var authFragBinding: FragmentAuthenticateMainBinding

    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleLogin: Button

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