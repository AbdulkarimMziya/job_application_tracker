package com.example.job_application_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.model.JobApplicationDatabase
import com.example.job_application_tracker.model.JobApplicationRepository

class RecentApplicationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: JobApplicationRepository

    private val _recentApplications = MutableLiveData<List<JobApplication>>()
    val recentApplications: LiveData<List<JobApplication>> get() = _recentApplications

    init {
        val jobApplicationDao = JobApplicationDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)

        // Observe all applications and filter
        repository.recentApplications.observeForever { applications ->
            filterRecentApplications(applications)
        }
    }

    private fun filterRecentApplications(applications: List<JobApplication>) {
        val oneWeekInMillis = 7 * 24 * 60 * 60 * 1000 // One week in milliseconds
        val oneWeekAgo = System.currentTimeMillis() - oneWeekInMillis
        _recentApplications.value = applications.filter { it.dateApplied >= oneWeekAgo }
    }
}