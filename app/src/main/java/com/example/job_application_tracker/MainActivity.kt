package com.example.job_application_tracker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Declare variables for the views
    private lateinit var tvWelcome: TextView
    private lateinit var imgLogo: ImageView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize the views using findViewById
        tvWelcome = findViewById(R.id.tvWelcome)
        imgLogo = findViewById(R.id.imgLogo)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin)

        // Set up button click listeners if needed
        btnSignIn.setOnClickListener {
            // Handle Sign In button click
        }

        btnSignUp.setOnClickListener {
            // Handle Sign Up button click
        }

        btnGoogleLogin.setOnClickListener {
            // Handle Google Login button click
        }
    }
}
