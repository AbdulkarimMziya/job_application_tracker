package com.example.job_application_tracker.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.job_application_tracker.R
import com.example.job_application_tracker.app_interfaces.BottomAppBarVisibility
import com.example.job_application_tracker.app_interfaces.FormFragment
import com.example.job_application_tracker.app_interfaces.FragmentNavigation
import com.example.job_application_tracker.databinding.FragmentAddApplicationBinding
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.viewmodel.JobApplicationViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddApplicationFragment : Fragment(), FormFragment {
    private lateinit var addApplicationBinding: FragmentAddApplicationBinding
     private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    private val calendar = Calendar.getInstance()
    private var replyDate: Date = Date()  // Default to today's date
    private var interviewDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addApplicationBinding = FragmentAddApplicationBinding.inflate(inflater, container, false)

        init()

        addApplicationBinding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (activity as? BottomAppBarVisibility)?.showBottomAppBar()
        }

        addApplicationBinding.btnSave.setOnClickListener {
            insertDataToDatabase()

            (activity as? FragmentNavigation)?.loadFragment(ApplicationScreenFragment())
            (activity as? BottomAppBarVisibility)?.showBottomAppBar()
        }

        return addApplicationBinding.root
    }

    private fun init() {
        // Hide BottomAppBar when entering this fragment
        (parentFragment as? BottomAppBarVisibility)?.hideBottomAppBar()

        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        setupStatusSpinner()
        setupDatePickers()
        // Initialize buttons with today's date
        updateDateButton(addApplicationBinding.btnPickDate, calendar.time)
        updateReplyButton()
    }

    private fun setupStatusSpinner() {
        val spinner = addApplicationBinding.spinnerApplicationStatus
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.application_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val scaleUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = parent.getItemAtPosition(position).toString()
                handleStatusSelection(selectedStatus)

                spinner.startAnimation(scaleUpAnimation)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }

        }
    }

    private fun handleStatusSelection(status: String) {
        if (status == "Interview") {
            addApplicationBinding.lblInterviewDate.visibility = View.VISIBLE
            addApplicationBinding.btnInterviewDate.isEnabled = true
        } else {
            addApplicationBinding.lblInterviewDate.visibility = View.GONE
            addApplicationBinding.btnInterviewDate.isEnabled = false
        }
    }

    private fun setupDatePickers() {
        val scaleUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        // Set a click listener on the application date button
        addApplicationBinding.btnPickDate.setOnClickListener {
            it.startAnimation(scaleUpAnimation)
            showDatePicker { date ->
                calendar.time = date
                updateDateButton(addApplicationBinding.btnPickDate, calendar.time)
            }
        }


        // Set a click listener on the reply date button
        addApplicationBinding.btnReplyDate.setOnClickListener {
            it.startAnimation(scaleUpAnimation)
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
        addApplicationBinding.btnInterviewDate.setOnClickListener {
            it.startAnimation(scaleUpAnimation)
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
        updateDateButton(addApplicationBinding.btnReplyDate, replyDate)
    }

    private fun updateInterviewButton() {
        updateDateButton(addApplicationBinding.btnInterviewDate, interviewDate ?: Date())
    }

    private fun insertDataToDatabase() {
        val companyName = addApplicationBinding.tfCompanyName.editText?.text.toString()
        val jobTitle = addApplicationBinding.tfJobTittle.editText?.text.toString()
        val location = addApplicationBinding.tfLocation.editText?.text.toString()
        val status = addApplicationBinding.spinnerApplicationStatus.selectedItem.toString()
        val link = addApplicationBinding.tfLink.editText?.text.toString()
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
