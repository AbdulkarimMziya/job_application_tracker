package com.example.job_application_tracker.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.job_application_tracker.R
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.app_interfaces.FormFragment
import com.example.job_application_tracker.databinding.FragmentEditApplicationBinding
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.viewmodel.JobApplicationViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditApplicationFragment : Fragment(), FormFragment {
    private lateinit var _binding: FragmentEditApplicationBinding
    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    private lateinit var jobApplication: JobApplication
    private val calendar = Calendar.getInstance()
    private var replyDate: Date = Date()
    private var interviewDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditApplicationBinding.inflate(inflater, container, false)



        return _binding.root
    }

    private fun init() {
        // Hide BottomAppBar when entering this fragment
        (parentFragment as? BottomAppBarVisibility)?.hideBottomAppBar()

        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        setupStatusSpinner()
        setupDatePickers()
        // Initialize buttons with today's date
        updateDateButton(_binding.btnEditPickDate, calendar.time)
        updateReplyButton()
    }

    private fun setupStatusSpinner() {
        val spinner = _binding.spinnerEditApplicationStatus
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.application_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = parent.getItemAtPosition(position).toString()
                handleStatusSelection(selectedStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun handleStatusSelection(status: String) {
        if (status == "Interview") {
            _binding.lblInterviewDate.visibility = View.VISIBLE
            _binding.btnInterviewDate.isEnabled = true
        } else {
            _binding.lblInterviewDate.visibility = View.GONE
            _binding.btnInterviewDate.isEnabled = false
        }
    }

    private fun setupDatePickers() {
        // Set a click listener on the application date button
        _binding.btnEditPickDate.setOnClickListener {
            showDatePicker { date ->
                calendar.time = date
                updateDateButton(_binding.btnEditPickDate, calendar.time)
            }
        }

        // Set a click listener on the reply date button
        _binding.btnEditReplyDate.setOnClickListener {
            showDatePicker { date ->
                if (date.before(calendar.time)) {
                    replyDate = Date()
                    updateReplyButton()
                    Toast.makeText(requireContext(), "Reply date cannot be before application date", Toast.LENGTH_SHORT).show()
                } else {
                    replyDate = date
                    updateReplyButton()
                }
            }
        }

        // Set a click listener on the interview date button
        _binding.btnInterviewDate.setOnClickListener {
            showDatePicker { date ->
                interviewDate = date
                updateInterviewButton()
            }
        }
    }

    private fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateButton(button: Button, date: Date) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        button.text = dateFormat.format(date)  // Update button text with the selected date
    }

    private fun updateReplyButton() {
        updateDateButton(_binding.btnEditReplyDate, replyDate)
    }

    private fun updateInterviewButton() {
        updateDateButton(_binding.btnInterviewDate, interviewDate ?: Date())
    }

    private fun insertDataToDatabase() {
        val companyName = _binding.tfEditCompanyName.editText?.text.toString()
        val jobTitle = _binding.tfEditJobTittle.editText?.text.toString()
        val location = _binding.tfEditLocation.editText?.text.toString()
        val status = _binding.spinnerEditApplicationStatus.selectedItem.toString()
        val link = _binding.tfEditLink.editText?.text.toString()
        val dateApplied = calendar.timeInMillis
        val replyDateString = replyDate.time
        val interviewDateString = interviewDate?.time ?: 0L

        // Create JobApplication instance
        val jobApplication = JobApplication(
            id = 0,  // Assuming id is auto-generated
            companyName = companyName,
            jobTitle = jobTitle,
            location = location,
            link = link,
            status = status,
            dateApplied = dateApplied,
            dateLatestReply = replyDateString,
            interviewDate = interviewDateString
        )

        // Save to ViewModel
        mJobApplicationViewModel.addJobApplication(jobApplication)
        Toast.makeText(requireContext(), "Application saved successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun isFormFragment(): Boolean {
        return true
    }




}
