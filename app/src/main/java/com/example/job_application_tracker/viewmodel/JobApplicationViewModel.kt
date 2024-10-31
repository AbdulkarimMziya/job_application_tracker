package com.example.job_application_tracker.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.job_application_tracker.model.JobApplication
import com.example.job_application_tracker.model.JobApplicationDatabase
import com.example.job_application_tracker.model.JobApplicationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobApplicationViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("JobAppPrefs", Context.MODE_PRIVATE)

    private val repository: JobApplicationRepository
    val applicationCount: LiveData<Int>

    private val _sortedJobApplications = MutableLiveData<List<JobApplication>>()
    val sortedJobApplications: LiveData<List<JobApplication>> = _sortedJobApplications

    init {
        val jobApplicationDao = JobApplicationDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)
        applicationCount = repository.applicationCount

        // Load saved sort criteria and apply it
        val savedSortCriteria = sharedPreferences.getString("sort_criteria", "date_desc") ?: "date_desc"
        loadSortedApplications(savedSortCriteria)
    }

    private fun loadSortedApplications(criteria: String) {
        repository.sortApplications(criteria).observeForever { sortedApplications ->
            _sortedJobApplications.value = sortedApplications
        }
    }

    fun addJobApplication(jobApplication: JobApplication) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(jobApplication)
        }
    }

    fun delete(jobApplication: JobApplication) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(jobApplication)
        }
    }

    fun getCountOfRejectedApplications(): LiveData<Int> {
        val countLiveData = MutableLiveData<Int>()
        viewModelScope.launch {
            val count = repository.getCountByStatus("Rejected")
            countLiveData.postValue(count)
        }
        return countLiveData
    }

    fun getCountOfInterviews(): LiveData<Int> {
        val countLiveData = MutableLiveData<Int>()
        viewModelScope.launch {
            val count = repository.getCountByStatus("Interview")
            countLiveData.postValue(count)
        }
        return countLiveData
    }

    fun getCountOfOffers(): LiveData<Int> {
        val countLiveData = MutableLiveData<Int>()
        viewModelScope.launch {
            val count = repository.getCountByStatus("Offer")
            countLiveData.postValue(count)
        }
        return countLiveData
    }

    fun searchDatabase(searchQuery: String): LiveData<List<JobApplication>> {
        return repository.searchDatabase("%$searchQuery%")
    }

    fun sortApplications(criteria: String): LiveData<List<JobApplication>> {
        // Save the selected criteria in SharedPreferences
        sharedPreferences.edit().putString("sort_criteria", criteria).apply()

        // Load sorted applications based on new criteria
        loadSortedApplications(criteria)

        return sortedJobApplications
    }
}
