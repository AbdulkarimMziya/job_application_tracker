package com.example.job_application_tracker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job_application_tracker.R
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.viewmodel.NotificationViewModel
import com.example.job_application_tracker.viewmodel.adapters.JobListAdapter

class NotificationViewPagerFragment : Fragment() {
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var adapter: JobListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noNotificationsTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.recyclerViewNotifications)
        noNotificationsTextView = view.findViewById(R.id.textView) // Assuming you have this TextView in your layout

        adapter = JobListAdapter(
            deleteJobApplication = {},
            onItemClick = {},
            showArrow = false
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the ViewModel
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        // Observe notifications
        notificationViewModel.notifications.observe(viewLifecycleOwner) { applications ->
            updateUI(applications)
        }
    }

    private fun updateUI(applications: List<JobApplication>) {
        if (applications.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            noNotificationsTextView.visibility = View.VISIBLE
        } else {
            adapter.submitList(applications)
            recyclerView.visibility = View.VISIBLE
            noNotificationsTextView.visibility = View.INVISIBLE
        }
    }
}
