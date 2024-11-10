package com.example.job_application_tracker.views.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.job_application_tracker.R
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.app_interfaces.FragmentNavigation
import com.example.job_application_tracker.databinding.FragmentEditApplicationBinding
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.viewmodel.JobApplicationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditApplicationFragment : Fragment() {

    private lateinit var _binding: FragmentEditApplicationBinding
    private lateinit var jobApplication: JobApplication
    private val mJobApplicationViewModel: JobApplicationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditApplicationBinding.inflate(inflater, container, false)

        // Hide BottomAppBar when entering this fragment
        (activity as? BottomAppBarVisibility)?.hideBottomAppBar()

        // Retrieve the job application data from the arguments
        arguments?.let {
            jobApplication = it.getParcelable("jobApplication") ?: return@let
            preFillFields(jobApplication)
        }

        _binding.lblEditInterviewDate.visibility = View.VISIBLE


        // Save Button Logic (Save changes to the job application)
        _binding.btnSave.setOnClickListener {
            saveJobApplication()
        }

        _binding.btnDeleteApplication.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Cancel Button Logic (Navigate back to the previous screen)
        _binding.btnCancel.setOnClickListener {
            (activity as? FragmentNavigation)?.loadFragment(ApplicationScreenFragment())
            (activity as? BottomAppBarVisibility)?.showBottomAppBar()
        }

        // Date Picker for applied date
        _binding.btnEditPickDate.setOnClickListener {
            showDatePicker { selectedDate ->
                jobApplication = jobApplication.copy(dateApplied = selectedDate.time)
                updateAppliedDateButton(selectedDate)
            }
        }

        // Date Picker for reply date
        _binding.btnEditReplyDate.setOnClickListener {
            showDatePicker { selectedDate ->
                jobApplication = jobApplication.copy(dateLatestReply = selectedDate.time)
                updateReplyDateButton(selectedDate)
            }
        }

        // Date Picker for interview date
        _binding.btnEditInterviewDate.setOnClickListener {
            showDatePicker { selectedDate ->
                jobApplication = jobApplication.copy(interviewDate = selectedDate.time)
                updateInterviewDateButton(selectedDate)
            }
        }

        // Handle the spinner selection for the status
        _binding.spinnerEditApplicationStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = parentView.getItemAtPosition(position).toString()
                handleStatusSelection(selectedStatus)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing if no item is selected
            }
        }

        return _binding.root
    }

    private fun preFillFields(jobApplication: JobApplication) {
        _binding.apply {
            tfEditCompanyName.editText?.setText(jobApplication.companyName)
            tfEditJobTittle.editText?.setText(jobApplication.jobTitle)
            tfEditLocation.editText?.setText(jobApplication.location)
            tfEditLink.editText?.setText(jobApplication.link)

            val statusAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.application_status,
                android.R.layout.simple_spinner_item
            )
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEditApplicationStatus.adapter = statusAdapter

            val statusPosition = statusAdapter.getPosition(jobApplication.status)
            spinnerEditApplicationStatus.setSelection(statusPosition)

            handleStatusSelection(jobApplication.status)

            jobApplication.dateApplied?.let {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                btnEditPickDate.text = dateFormat.format(Date(it))
            }

            jobApplication.dateLatestReply?.let {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                btnEditReplyDate.text = dateFormat.format(Date(it))
            }

            jobApplication.interviewDate?.let {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                btnEditInterviewDate.text = dateFormat.format(Date(it))
            }
        }
    }

    private fun handleStatusSelection(status: String) {
        if (status.equals("Interview", ignoreCase = true)) {
            _binding.lblEditInterviewDate.visibility = View.VISIBLE
            _binding.btnEditInterviewDate.isEnabled = true  // Enable the interview date button
        } else {
            _binding.lblEditInterviewDate.visibility = View.GONE
            _binding.btnEditInterviewDate.isEnabled = false  // Disable the interview date button
        }
    }

    private fun saveJobApplication() {
        val updatedJobApplication = jobApplication.copy(
            companyName = _binding.tfEditCompanyName.editText?.text.toString(),
            jobTitle = _binding.tfEditJobTittle.editText?.text.toString(),
            location = _binding.tfEditLocation.editText?.text.toString(),
            status = _binding.spinnerEditApplicationStatus.selectedItem.toString(),
            interviewDate = jobApplication.interviewDate,  // Save interview date if selected
            dateLatestReply = jobApplication.dateLatestReply  // Save reply date if selected
        )

        mJobApplicationViewModel.updateJobApplication(updatedJobApplication)

        // Show a Toast message to confirm that the application is updated
        Toast.makeText(requireContext(), "Job Application Updated", Toast.LENGTH_SHORT).show()

        // Navigate back to the previous screen or fragment
        (activity as? FragmentNavigation)?.loadFragment(ApplicationScreenFragment())
        (activity as? BottomAppBarVisibility)?.showBottomAppBar()
    }

    private fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val initialDate = jobApplication.dateApplied?.let {
            Calendar.getInstance().apply { time = Date(it) }
        } ?: calendar

        val year = initialDate.get(Calendar.YEAR)
        val month = initialDate.get(Calendar.MONTH)
        val dayOfMonth = initialDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDayOfMonth)
                }.time

                onDateSelected(selectedDate)
            },
            year, month, dayOfMonth
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }

    private fun updateAppliedDateButton(selectedDate: Date) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        _binding.btnEditPickDate.text = dateFormat.format(selectedDate)
    }

    private fun updateInterviewDateButton(selectedDate: Date) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        _binding.btnEditInterviewDate.text = dateFormat.format(selectedDate)
    }

    private fun updateReplyDateButton(selectedDate: Date) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        _binding.btnEditReplyDate.text = dateFormat.format(selectedDate)
    }

    private fun showDeleteConfirmationDialog() {
        // This will show a simple confirmation dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this job application?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                deleteJobApplication()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()  // Dismiss the dialog
            }

        builder.create().show()
    }

    private fun deleteJobApplication() {
        // Call the delete method in the ViewModel
        mJobApplicationViewModel.delete(jobApplication)
        Toast.makeText(requireContext(), "Job Application Deleted", Toast.LENGTH_SHORT).show()

        // Navigate back to the previous screen after deletion
        (activity as? FragmentNavigation)?.loadFragment(ApplicationScreenFragment())
        (activity as? BottomAppBarVisibility)?.showBottomAppBar()
    }
}

