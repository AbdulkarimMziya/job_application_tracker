package com.example.job_application_tracker.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface JobApplicationDao {

    @Insert
    suspend fun insert(jobApplication: JobApplication)

    @Query("SELECT * FROM job_application")
    fun getAllApplications(): LiveData<List<JobApplication>>
}