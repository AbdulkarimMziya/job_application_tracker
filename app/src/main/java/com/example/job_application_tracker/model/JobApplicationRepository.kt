package com.example.job_application_tracker.model

import androidx.lifecycle.LiveData

class JobApplicationRepository(val jobApplicationDao: JobApplicationDao) {

    val allJobApplications: LiveData<List<JobApplication>> = jobApplicationDao.getAllApplications()
    val applicationCount: LiveData<Int> = jobApplicationDao.getApplicationCount()


    suspend fun insert(jobApplication: JobApplication){
        jobApplicationDao.insert(jobApplication)
    }

    suspend fun getCountByStatus(status: String): Int {
        return jobApplicationDao.getCountByStatus(status)
    }

}