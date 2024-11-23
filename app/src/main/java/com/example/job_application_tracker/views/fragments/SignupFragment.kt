package com.example.job_application_tracker.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment() {

    private lateinit var signupFragBinding: FragmentSignupBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth

    private val TAG = "SignupFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signupFragBinding = FragmentSignupBinding.inflate(inflater, container, false)
        init()
        setupListeners()

        return signupFragBinding.root
    }

    private fun init() {
        etEmail = signupFragBinding.etEmail.editText!!
        etPassword = signupFragBinding.etPassword.editText!!
        etConfirmPassword = signupFragBinding.etConfirmPassword.editText!!
        btnRegister = signupFragBinding.btnRegister

        auth = FirebaseAuth.getInstance()
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Check if any of the fields are empty
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                // Check if passwords match
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Validate email format
                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else {
                // If validation passes, attempt registration
                registerUser(email, password)

            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                    // Navigate to the Sign In fragment after successful registration
                    view?.findNavController()?.navigate(R.id.action_signupFragment_to_signInFragment)

                } else {
                    // Registration failed, handle the exception and show appropriate message
                    val errorMessage = task.exception?.message ?: "Authentication failed"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

}
