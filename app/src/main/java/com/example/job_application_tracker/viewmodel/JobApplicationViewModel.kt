package com.example.job_application_tracker.viewmodel

import android.app.Application
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
    val readAllJobApplications: LiveData<List<JobApplication>>
    private val repository: JobApplicationRepository

    val applicationCount: LiveData<Int>

    init {
        val jobApplicationDao = JobApplicationDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)
        readAllJobApplications = repository.allJobApplications
        applicationCount = repository.applicationCount
    }

    fun addJobApplication(jobApplication: JobApplication){
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

}