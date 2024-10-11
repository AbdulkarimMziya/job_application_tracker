package com.example.job_application_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.job_application_tracker.databinding.FragmentForgotPasswordScreenBinding


class ForgotPasswordScreenFragment : Fragment() {
    private lateinit var forgotPasswordScreenBinding: FragmentForgotPasswordScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forgotPasswordScreenBinding  = FragmentForgotPasswordScreenBinding.inflate(layoutInflater,container,false)

        forgotPasswordScreenBinding.btnUpdateUserPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_forgotPasswordScreenFragment_to_loginScreenFragment)
        }


        // Inflate the layout for this fragment
        return forgotPasswordScreenBinding.root
    }

}