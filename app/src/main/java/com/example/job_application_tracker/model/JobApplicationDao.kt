package com.example.job_application_tracker.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface JobApplicationDao {

    @Insert
    suspend fun insert(jobApplication: JobApplication)

    @Delete
    suspend fun delete(jobApplication: JobApplication)

    @Query("SELECT * FROM job_application ORDER BY date_applied DESC")
    fun getAllApplications(): LiveData<List<JobApplication>>

    @Query("SELECT * FROM job_application ORDER BY date_applied DESC LIMIT 3")
    fun getRecentApplications(): LiveData<List<JobApplication>>

    @Query("SELECT COUNT(id) FROM job_application ")
    fun getApplicationCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM job_application WHERE status = :status")
    suspend fun getCountByStatus(status: String): Int

    @Query("SELECT * FROM job_application WHERE status = 'Interview' AND date_of_interview > :currentTime")
    fun getUpcomingInterviews(currentTime: Long): LiveData<List<JobApplication>>
}