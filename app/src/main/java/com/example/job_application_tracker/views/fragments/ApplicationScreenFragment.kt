package com.example.job_application_tracker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.FragmentAddApplicationBinding
import com.example.job_application_tracker.databinding.FragmentApplicationScreenBinding
import com.example.job_application_tracker.viewmodel.JobApplicationViewModel
import com.example.job_application_tracker.viewmodel.JobListAdapter

class ApplicationScreenFragment : Fragment() {
    private lateinit var applicationScreenBinding: FragmentApplicationScreenBinding

    private lateinit var searchView: SearchView
    private lateinit var topBarLayout: View

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobListAdapter: JobListAdapter
    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        applicationScreenBinding = FragmentApplicationScreenBinding.inflate(layoutInflater,container,false)

        init()




        return applicationScreenBinding.root
    }

    private fun init() {
        topBarLayout = applicationScreenBinding.topBarLayout
        searchView = applicationScreenBinding.searchView
        recyclerView = applicationScreenBinding.recyclerView

        // Initialize the ViewModel
        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        // Initialize the RecyclerView and Adapter
        jobListAdapter = JobListAdapter { jobApplication ->
            // Handle item click
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = jobListAdapter

        // Observe the job applications LiveData
        mJobApplicationViewModel.readAllJobApplications.observe(viewLifecycleOwner) { jobApplications ->
            jobListAdapter.submitList(jobApplications)
        }

        // Observe application count
        mJobApplicationViewModel.applicationCount.observe(viewLifecycleOwner) { count ->
            val applicationText = if (count == 1) {
                "$count Application"
            } else {
                "$count Applications"
            }
            applicationScreenBinding.txtDisplayTotalApplications.text = applicationText
        }

        // Set up the SearchView listener
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideTopBar()
            } else {
                showTopBar()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search action here if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text changes here if needed
                return true
            }
        })
    }

    private fun hideTopBar() {
        topBarLayout.visibility = View.GONE
    }

    private fun showTopBar() {
        topBarLayout.visibility = View.VISIBLE
    }

    private fun resetSearchView() {
        searchView.setQuery("", false) // Clear the search query
        searchView.clearFocus() // Clear focus from the search view
        showTopBar() // Show the top bar again
    }
}
