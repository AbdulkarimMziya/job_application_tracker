package com.example.job_application_tracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job_application_tracker.databinding.FragmentRecentApplicationViewPagerBinding
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.viewmodel.adapters.JobListAdapter
import com.example.job_application_tracker.viewmodel.RecentApplicationViewModel


class RecentApplicationViewPagerFragment : Fragment() {
    private lateinit var mViewModel: RecentApplicationViewModel
    private lateinit var adapter: JobListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noApplicationsTextView: TextView

    private lateinit var binding: FragmentRecentApplicationViewPagerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecentApplicationViewPagerBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView and Adapter
        initRecyclerView()

        // Set up the ViewModel
        mViewModel = ViewModelProvider(requireActivity()).get(RecentApplicationViewModel::class.java)

        // Observe recent applications
        mViewModel.recentApplications.observe(viewLifecycleOwner) { applications ->
            updateUI(applications)
        }
    }

    private fun initRecyclerView() {
        adapter = JobListAdapter(deleteJobApplication = {}, onItemClick = {}, showArrow = false)
        binding.recyclerViewRecentApplications.adapter = adapter
        binding.recyclerViewRecentApplications.layoutManager = LinearLayoutManager(context)
    }

    private fun updateUI(applications: List<JobApplication>) {
        if (applications.isEmpty()) {
            binding.recyclerViewRecentApplications.visibility = View.INVISIBLE
            binding.textView.visibility = View.VISIBLE
        } else {
            adapter.submitList(applications)
            binding.recyclerViewRecentApplications.visibility = View.VISIBLE
            binding.textView.visibility = View.INVISIBLE
        }
    }

}