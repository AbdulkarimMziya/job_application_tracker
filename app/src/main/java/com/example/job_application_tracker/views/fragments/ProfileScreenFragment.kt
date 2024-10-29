package com.example.job_application_tracker.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentProfileScreenBinding
import com.example.job_application_tracker.views.MainActivity


class ProfileScreenFragment : Fragment() {
    private lateinit var profileScreenBinding: FragmentProfileScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileScreenBinding = FragmentProfileScreenBinding.inflate(layoutInflater,container,false)


        profileScreenBinding.btnOpenAbdulLink.setOnClickListener {
            openUrl("https://abdulmziya.netlify.app")
        }
        profileScreenBinding.btnOpenGabrielLink.setOnClickListener {
            openUrl("https://www.linkedin.com/in/gabriel-eremie-0ab159250/")
        }


        return profileScreenBinding.root
    }

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent  = Intent(Intent.ACTION_VIEW,uri)

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()
    }

}