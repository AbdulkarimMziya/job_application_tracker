package com.akproduction.job_application_tracker.views.fragments

import SwipeToDeleteCallback
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akproduction.job_application_tracker.R
import com.akproduction.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.akproduction.job_application_tracker.app_interfaces.FragmentNavigation
import com.akproduction.job_application_tracker.databinding.FragmentApplicationScreenBinding
import com.akproduction.job_application_tracker.viewmodel.JobApplicationViewModel
import com.akproduction.job_application_tracker.viewmodel.adapters.JobListAdapter

class ApplicationScreenFragment : Fragment() {
    private lateinit var applicationScreenBinding: FragmentApplicationScreenBinding

    private lateinit var searchView: SearchView
    private lateinit var topBarLayout: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobListAdapter: JobListAdapter
    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    private lateinit var bannerLayout: LinearLayout

    private lateinit var popupMenu: PopupMenu
    private var selectedMenuItemId: Int = R.id.action_sort_newest_oldest  // Default to a selected item

    private lateinit var mFragmentNavigation: FragmentNavigation

    private lateinit var btnSortList: ImageView
    private lateinit var btnFilterList: ImageView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure that the parent activity implements FragmentNavigation
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        } else {
            throw ClassCastException("$context must implement FragmentNavigation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        applicationScreenBinding = FragmentApplicationScreenBinding.inflate(layoutInflater, container, false)



        init()

        return applicationScreenBinding.root
    }

    private fun init() {
        topBarLayout = applicationScreenBinding.topBarLayout
        searchView = applicationScreenBinding.searchView
        recyclerView = applicationScreenBinding.recyclerView
        bannerLayout = applicationScreenBinding.bannerLayout



        // Initialize the ViewModel
        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        // Initialize the RecyclerView and Adapter
        jobListAdapter = JobListAdapter(
            onItemClick = { jobApplication ->

                // Handle item click: Navigate to EditApplicationFragment
                val editApplicationFragment = EditApplicationFragment()

                // Create a Bundle to pass the JobApplication data
                val bundle = Bundle().apply {
                    putParcelable("jobApplication", jobApplication)
                }

                editApplicationFragment.arguments = bundle

                // Use the loadFragment method to load the EditApplicationFragment
                mFragmentNavigation.loadFragment(editApplicationFragment)
            },
            deleteJobApplication = { jobApplication ->
                mJobApplicationViewModel.delete(jobApplication)
            },
            showArrow = true
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = jobListAdapter

        // Attach the ItemTouchHelper for swipe to delete
        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(
                jobListAdapter,
                ContextCompat.getDrawable(requireContext(), R.drawable.red_background)!!,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!,
                Color.RED
            )
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Observe the job applications LiveData
        mJobApplicationViewModel.sortedJobApplications.observe(viewLifecycleOwner) { jobApplications ->
            // Toggle visibility based on job application count
            if (jobApplications.isEmpty()) {
                // No applications, show banner and hide RecyclerView
                recyclerView.visibility = View.GONE
                bannerLayout.visibility = View.VISIBLE
            } else {
                // Applications exist, show RecyclerView and hide banner
                bannerLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                jobListAdapter.submitList(jobApplications)
            }
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

        // Initialize the button
        btnSortList = applicationScreenBinding.btnSortList

        // Set the click listener for the button
        btnSortList.setOnClickListener {
            // Call your sort popup menu logic
            showSortPopupMenu(it)
        }

        btnFilterList = applicationScreenBinding.btnFilterList

        btnFilterList.setOnClickListener {
            showFilterPopupMenu(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? BottomAppBarVisibility)?.showBottomAppBar()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideTopBar()
                (activity as? BottomAppBarVisibility)?.hideBottomAppBar()
                applicationScreenBinding.btnCancelSearch.visibility = View.VISIBLE
            } else {
                showTopBar()
                (activity as? BottomAppBarVisibility)?.showBottomAppBar()
                applicationScreenBinding.btnCancelSearch.visibility = View.GONE
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Update the search results as the user types
                val searchQuery = newText?.trim() ?: ""

                // If the search query is empty, reset to the full list
                if (searchQuery.isEmpty()) {
                    // Show all job applications when there's no query
                    mJobApplicationViewModel.sortedJobApplications.observe(viewLifecycleOwner) { jobApplications ->
                        jobListAdapter.submitList(jobApplications)
                    }
                } else {
                    // Call the ViewModel to search the database with the updated query
                    mJobApplicationViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { jobApplications ->
                        jobListAdapter.submitList(jobApplications)
                    }
                }
                return true
            }
        })

        applicationScreenBinding.btnCancelSearch.setOnClickListener {
            resetSearchView() // Reset everything when the Cancel button is clicked
        }
    }

    private fun showSortPopupMenu(view: View) {
        // Create the PopupMenu and associate it with the anchor view (the button clicked)
        popupMenu = PopupMenu(requireContext(), view)

        // Inflate the menu items from the XML file using activity's menuInflater
        requireActivity().menuInflater.inflate(R.menu.sort_toolbar_menu, popupMenu.menu)

        // Set the checked item manually based on the selectedMenuItemId
        for (i in 0 until popupMenu.menu.size()) {
            val item = popupMenu.menu.getItem(i)
            item.isChecked = item.itemId == selectedMenuItemId
        }

        // Set a listener for item clicks in the PopupMenu
        popupMenu.setOnMenuItemClickListener { item ->
            // Store the selected item ID
            selectedMenuItemId = item.itemId

            // Uncheck all menu items
            for (i in 0 until popupMenu.menu.size()) {
                popupMenu.menu.getItem(i).isChecked = false
            }

            // Check the selected item
            item.isChecked = true

            // Apply sorting based on the selected item
            when (item.itemId) {
                R.id.action_sort_az -> {
                    applySorting("name_asc")
                    true
                }
                R.id.action_sort_za -> {
                    applySorting("name_desc")
                    true
                }
                R.id.action_sort_oldest_newest -> {
                    applySorting("date_asc")
                    true
                }
                R.id.action_sort_newest_oldest -> {
                    applySorting("date_desc")
                    true
                }
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    private fun applySorting(criteria: String) {
        // Perform sorting logic based on the selected criteria
        mJobApplicationViewModel.sortApplications(criteria).observe(viewLifecycleOwner) { sortedApplications ->
            jobListAdapter.submitList(sortedApplications)
        }
    }

    private fun showFilterPopupMenu(view: View) {
        popupMenu = PopupMenu(requireContext(), view)

        // Inflate the menu items from the XML file using activity's menuInflater
        requireActivity().menuInflater.inflate(R.menu.filter_toolbar_menu, popupMenu.menu)

        // Set the checked item manually based on the selectedMenuItemId
        for (i in 0 until popupMenu.menu.size()) {
            val item = popupMenu.menu.getItem(i)
            item.isChecked = item.itemId == selectedMenuItemId
        }

        popupMenu.setOnMenuItemClickListener { item ->
            selectedMenuItemId = item.itemId

            // Uncheck all menu items
            for (i in 0 until popupMenu.menu.size()) {
                popupMenu.menu.getItem(i).isChecked = false
            }

            item.isChecked = true

            // Apply filtering based on the selected item
            when (item.itemId) {
                R.id.action_filter_applied -> {
                    applyFilter("Applied")
                    true
                }
                R.id.action_filter_rejected -> {
                    applyFilter("Rejected")
                    true
                }
                R.id.action_filter_offer -> {
                    applyFilter("Offer")
                    true
                }
                R.id.action_filter_waiting -> {
                    applyFilter("Waiting")
                    true
                }
                R.id.action_filter_interview -> {
                    applyFilter("Interview")
                    true
                }
                R.id.action_filter_none -> {
                    showAllApplications()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun applyFilter(status: String) {
        // Call the ViewModel's filter function and observe the filtered data
        mJobApplicationViewModel.filterApplicationsByStatus(status).observe(viewLifecycleOwner) { jobApplications ->
            jobListAdapter.submitList(jobApplications)  // Update the list in the adapter
        }
    }

    private fun showAllApplications() {
        // This will call the repository method that returns all applications (no filter)
        mJobApplicationViewModel.sortedJobApplications.observe(viewLifecycleOwner) { jobApplications ->
            jobListAdapter.submitList(jobApplications)  // Update the list in the adapter
        }
    }


    fun focusSearchView() {
        view?.post {
            searchView.requestFocus() // Focus on the SearchView after the layout is ready
            Log.d("SearchNav", "Search function Working")
        }
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

        // Re-observe the full list of job applications
        mJobApplicationViewModel.sortedJobApplications.observe(viewLifecycleOwner) { jobApplications ->
            jobListAdapter.submitList(jobApplications)
        }
    }
}
