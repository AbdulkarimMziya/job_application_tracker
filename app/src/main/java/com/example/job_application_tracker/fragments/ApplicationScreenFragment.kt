package com.example.job_application_tracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.job_application_tracker.R

class ApplicationScreenFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var topBarLayout: View
    private lateinit var cancelButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_application_screen, container, false)

        searchView = view.findViewById(R.id.searchView)
        topBarLayout = view.findViewById(R.id.topBarLayout)
        cancelButton = view.findViewById(R.id.btnCancelSearch)

        // Set up the SearchView listener
        setupSearchView()

        // Handle cancel button click
        cancelButton.setOnClickListener {

        }

        return view
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideTopBar()
                cancelButton.visibility = View.VISIBLE
            } else {
                showTopBar()
                cancelButton.visibility = View.GONE
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
        cancelButton.visibility = View.GONE // Hide the cancel button
    }
}
