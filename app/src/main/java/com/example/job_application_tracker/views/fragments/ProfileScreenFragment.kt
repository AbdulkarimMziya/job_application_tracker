package com.example.job_application_tracker.views.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentProfileScreenBinding




class ProfileScreenFragment : Fragment() {
    private lateinit var profileScreenBinding: FragmentProfileScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileScreenBinding = FragmentProfileScreenBinding.inflate(layoutInflater, container, false)


        // Set onClickListeners for the links with scale-up effect
        profileScreenBinding.btnOpenAbdulLink.setOnClickListener {
            // Apply scale-up animation to Abdul's ImageView
            applyScaleUpAnimation(profileScreenBinding.ivFollowMe)
            openUrl("https://abdulmziya.netlify.app")
        }


        profileScreenBinding.btnOpenGabrielLink.setOnClickListener {
            // Apply scale-up animation to Gabriel's ImageView
            applyScaleUpAnimation(profileScreenBinding.ivFollowMe1)
            openUrl("https://www.linkedin.com/in/gabriel-eremie-0ab159250/")
        }


        return profileScreenBinding.root
    }


    // Function to apply the scale-up animation from XML
    private fun applyScaleUpAnimation(imageView: View) {
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        imageView.startAnimation(scaleUp)
    }


    // Function to open a URL
    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()
    }
}
