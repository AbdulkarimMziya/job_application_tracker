package com.akproduction.job_application_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.akproduction.job_application_tracker.model.JobApplication
import com.akproduction.job_application_tracker.model.JobApplicationDatabase
import com.akproduction.job_application_tracker.model.JobApplicationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: JobApplicationRepository
    val notifications: LiveData<List<JobApplication>>


    init {
        val jobApplicationDao = JobApplicationDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)
        notifications = repository.getUpcomingInterviews()
    }
}