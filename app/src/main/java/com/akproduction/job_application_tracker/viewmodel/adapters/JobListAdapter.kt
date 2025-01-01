package com.akproduction.job_application_tracker.viewmodel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akproduction.job_application_tracker.R
import com.akproduction.job_application_tracker.databinding.ListItemLayoutBinding
import com.akproduction.job_application_tracker.model.JobApplication
import java.text.SimpleDateFormat
import java.util.*

class JobListAdapter(
    private val onItemClick: (JobApplication) -> Unit,
    private val deleteJobApplication: (JobApplication) -> Unit,
    private val showArrow: Boolean
) : ListAdapter<JobApplication, JobListAdapter.MyViewHolder>(JobApplicationDiffCallback()) {

    class MyViewHolder(private val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jobApplication: JobApplication, onItemClick: (JobApplication) -> Unit, showArrow: Boolean) {
            binding.tvListItemCompanyName.text = jobApplication.companyName
            binding.tvListItemJobDesc.text = jobApplication.jobTitle
            binding.tvListItemDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(jobApplication.dateApplied))

            // Control arrow visibility based on the parameter
            binding.btnOpenListItem.visibility = if (showArrow) View.VISIBLE else View.INVISIBLE

            binding.tvListItemStatus.text = jobApplication.status
            binding.tvListItemStatus.setBackgroundResource(

                when (jobApplication.status.lowercase()) {
                "applied" -> R.drawable.round_status_yellow_secondary
                "waiting" -> R.drawable.round_back_white_secondary_10
                "rejected" -> R.drawable.round_status_red_secondary
                "interview" -> R.drawable.round_status_blue_secondary
                "offer" -> R.drawable.round_status_green_secondary
                else -> -1
            })

            binding.root.setOnClickListener { onItemClick(jobApplication) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val jobApplication = getItem(position)
        holder.bind(jobApplication, onItemClick, showArrow)
    }

    override fun getItemCount(): Int = currentList.size // Returns the size of the current list

    fun onItemSwipe(position: Int) {
        val jobApplication = getItem(position)
        deleteJobApplication(jobApplication) // Call the delete function
        notifyItemRemoved(position)
    }

    // Method to update the job list
    fun updateJobList(newJobList: List<JobApplication>?) {
        submitList(newJobList ?: emptyList()) // Ensure non-null
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
