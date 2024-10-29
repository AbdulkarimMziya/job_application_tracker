package com.example.job_application_tracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.databinding.FragmentHomeScreenBinding
import com.example.job_application_tracker.viewmodel.JobApplicationViewModel


class HomeScreenFragment : Fragment() {

    private lateinit var homeScreenBinding: FragmentHomeScreenBinding

    private lateinit var tvTotalApplications: TextView
    private lateinit var tvRejectedApplications: TextView
    private lateinit var tvTotalInterviews: TextView
    private lateinit var tvTotalOffers: TextView

    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding = FragmentHomeScreenBinding.inflate(layoutInflater,container,false)

        init()




        // Inflate the layout for this fragment
        return homeScreenBinding.root
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()
    }

    private fun init(){
        tvTotalApplications = homeScreenBinding.tvTotalApplications
        tvRejectedApplications =  homeScreenBinding.tvRejectedApplications
        tvTotalInterviews = homeScreenBinding.tvTotalInterviews
        tvTotalOffers = homeScreenBinding.tvTotalOffers

        // Initialize the ViewModel
        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        displayBoard()
    }

    private fun displayBoard(){

        // Observe application count
        mJobApplicationViewModel.applicationCount.observe(viewLifecycleOwner) { count ->
            tvTotalApplications.text = "$count"
        }

        // Observe rejected application count
        mJobApplicationViewModel.getCountOfRejectedApplications().observe(viewLifecycleOwner, { count ->
            tvRejectedApplications.text = "$count"
        })

        // Observe interview count
        mJobApplicationViewModel.getCountOfInterviews().observe(viewLifecycleOwner, { count ->
            tvTotalInterviews.text = "$count"
        })

        // Observe offers count
        mJobApplicationViewModel.getCountOfOffers().observe(viewLifecycleOwner, { count ->
            tvTotalOffers.text = "$count"
        })
    }

}