package com.example.job_application_tracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.job_application_tracker.databinding.FragmentEditFormScreenBinding


class EditFormScreenFragment : Fragment() {
    private lateinit var editFormScreenFragment: FragmentEditFormScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editFormScreenFragment = FragmentEditFormScreenBinding.inflate(layoutInflater,container,false)


        // Hide BottomAppBar when entering this fragment
        (parentFragment as? BottomAppBarVisibility)?.hideBottomAppBar()

        editFormScreenFragment.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }








        // Inflate the layout for this fragment
        return editFormScreenFragment.root
    }

}