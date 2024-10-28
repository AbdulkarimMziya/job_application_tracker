package com.example.job_application_tracker.model

import androidx.lifecycle.LiveData

class JobApplicationRepository(val jobApplicationDao: JobApplicationDao) {

    val allJobApplications: LiveData<List<JobApplication>> = jobApplicationDao.getAllApplications()

    suspend fun insert(jobApplication: JobApplication){
        jobApplicationDao.insert(jobApplication)
    }

}