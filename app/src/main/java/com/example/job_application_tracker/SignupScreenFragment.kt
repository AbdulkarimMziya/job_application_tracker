package com.example.job_application_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.job_application_tracker.databinding.FragmentSignupScreenBinding


class SignupScreenFragment : Fragment() {
    private lateinit var signupScreenBinding: FragmentSignupScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signupScreenBinding  = FragmentSignupScreenBinding.inflate(layoutInflater,container,false)

        signupScreenBinding.btnRegisterUser.setOnClickListener {
            it.findNavController().navigate(R.id.action_signupScreenFragment_to_loginScreenFragment)
        }
        // Inflate the layout for this fragment
        return signupScreenBinding.root
    }


}