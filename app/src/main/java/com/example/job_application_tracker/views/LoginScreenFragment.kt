package com.example.job_application_tracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentLoginScreenBinding


class LoginScreenFragment : Fragment() {
    private lateinit var loginScreenBinding: FragmentLoginScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginScreenBinding = FragmentLoginScreenBinding.inflate(layoutInflater,container,false)

        loginScreenBinding.tvLinkToRegisterPage.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginScreenFragment_to_signupScreenFragment)
        }

        loginScreenBinding.tvLinkToForgotPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginScreenFragment_to_forgotPasswordScreenFragment)
        }

        loginScreenBinding.btnLoginUser.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginScreenFragment_to_homeScreenFragment)
        }

        return loginScreenBinding.root
    }

}