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

    @Query("SELECT COUNT(id) FROM job_application ")
    fun getApplicationCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM job_application WHERE status = :status")
    suspend fun getCountByStatus(status: String): Int
}