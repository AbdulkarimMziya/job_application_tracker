package com.example.job_application_tracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "job_application")
@Parcelize
data  class JobApplication(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "company_name") val companyName: String,

    @ColumnInfo(name = "job_title") val jobTitle: String,

    @ColumnInfo(name = "location") val location: String,

    @ColumnInfo(name = "status") val status: String,

    @ColumnInfo(name = "link") val link: String,

    @ColumnInfo(name = "date_applied") val dateApplied: Long,

    @ColumnInfo(name = "date_latest_reply") val dateLatestReply: Long,

    @ColumnInfo(name = "date_of_interview") val interviewDate: Long?
    ) : Parcelable