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
import androidx.navigation.fragment.findNavController
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private lateinit var signInBinding: FragmentSignInBinding

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    private val TAG = "SignInFragment"

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignInBinding.inflate(inflater, container, false)

        init()  // Initialize views and FirebaseAuth
        setupListeners()  // Set up listeners for the buttons

        return signInBinding.root
    }

    private fun init() {
        // Initialize views
        etEmail = signInBinding.etEmail.editText!!
        etPassword = signInBinding.etPassword.editText!!
        btnLogin = signInBinding.login

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validation checks
            if (email.isEmpty() || password.isEmpty()) {
                // Check if any of the fields are empty
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Validate email format
                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else {
                // If validation passes, attempt sign-in
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        // Attempt to sign in the user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign-in successful
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(context, "Sign-in Successful", Toast.LENGTH_SHORT).show()

                    // Navigate to the Home or Main screen after successful sign-in
                    view?.findNavController()?.navigate(R.id.action_signInFragment_to_mainActivity)

                } else {
                    // Sign-in failed, handle the exception and show appropriate message
                    val errorMessage = task.exception?.message ?: "Authentication failed"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

}
