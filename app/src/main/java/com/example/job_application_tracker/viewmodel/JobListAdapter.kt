package com.example.job_application_tracker.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.job_application_tracker.R
import com.example.job_application_tracker.databinding.ListItemLayoutBinding
import com.example.job_application_tracker.model.JobApplication
import java.text.SimpleDateFormat
import java.util.*

class JobListAdapter(private val onItemClick: (JobApplication) -> Unit) :
    ListAdapter<JobApplication, JobListAdapter.MyViewHolder>(JobApplicationDiffCallback()) {

    class MyViewHolder(private val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jobApplication: JobApplication, onItemClick: (JobApplication) -> Unit) {
            binding.tvListItemCompanyName.text = jobApplication.companyName
            binding.tvListItemJobDesc.text = jobApplication.jobTitle
            binding.tvListItemDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(jobApplication.dateApplied))


            binding.tvListItemStatus.text = jobApplication.status

            when (jobApplication.status.lowercase()) {
                "applied" -> {
                    binding.tvListItemStatus.setBackgroundResource(R.drawable.round_status_yellow_secondary)
                }
                "waiting" -> {
                    binding.tvListItemStatus.setBackgroundResource(R.drawable.round_back_white_secondary_10)
                }
                "rejected" -> {
                    binding.tvListItemStatus.setBackgroundResource(R.drawable.round_status_red_secondary)
                }
                "interview" -> {
                    binding.tvListItemStatus.setBackgroundResource(R.drawable.round_status_blue_secondary)
                }
                "offer" -> {
                    binding.tvListItemStatus.setBackgroundResource(R.drawable.round_status_green_secondary
                    )
                }
            }

            binding.root.setOnClickListener { onItemClick(jobApplication) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val jobApplication = getItem(position)
        holder.bind(jobApplication, onItemClick)
    }

    override fun getItemCount(): Int {
        return currentList.size // Returns the size of the current list
    }

    // Method to update the job list
    fun updateJobList(newJobList: List<JobApplication>) {
        submitList(newJobList) // Use ListAdapter's submitList to handle updates
    }

    // DiffUtil for better performance and item updates
    class JobApplicationDiffCallback : DiffUtil.ItemCallback<JobApplication>() {
        override fun areItemsTheSame(oldItem: JobApplication, newItem: JobApplication): Boolean {
            return oldItem.id == newItem.id // Compare IDs
        }

        override fun areContentsTheSame(oldItem: JobApplication, newItem: JobApplication): Boolean {
            return oldItem == newItem // Compare the content
        }
    }
}
