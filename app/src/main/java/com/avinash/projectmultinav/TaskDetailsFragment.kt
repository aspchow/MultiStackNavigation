package com.avinash.projectmultinav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avinash.projectmultinav.databinding.TaskDetailsFragmentBinding

class TaskDetailsFragment : Fragment() {

    lateinit var binding: TaskDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printMsg("Inside the oncreate view")
        binding = TaskDetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        printMsg("On destroy is called ")
    }
}