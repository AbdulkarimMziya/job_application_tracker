package com.example.job_application_tracker.model

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [JobApplication::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class JobApplicationDatabase : RoomDatabase() {
    abstract fun jobApplicationDao(): JobApplicationDao

    companion object {
        @Volatile
        private var INSTANCE: JobApplicationDatabase? = null

        fun getDatabase(context: Context): JobApplicationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobApplicationDatabase::class.java,
                    "job_application_database"

                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

}