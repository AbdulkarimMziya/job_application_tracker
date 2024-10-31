package com.example.job_application_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.model.JobApplicationDao
import com.example.job_application_tracker.model.JobApplicationDatabase
import com.example.job_application_tracker.model.JobApplicationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: JobApplicationRepository
    val notifications: LiveData<List<JobApplication>>


    init {
        val jobApplicationDao = JobApplicationDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)
        notifications = repository.getUpcomingInterviews()
    }
}