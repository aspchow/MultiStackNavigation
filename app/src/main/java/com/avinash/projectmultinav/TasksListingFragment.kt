package com.avinash.projectmultinav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avinash.projectmultinav.databinding.TasksListingFragmentBinding


class TasksListingFragment : Fragment() {

    lateinit var binding: TasksListingFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TasksListingFragmentBinding.inflate(inflater).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openTaskDetailBtn.setOnClickListener {
            findNavController().navigate(TasksListingFragmentDirections.actionGlobalTaskDetailsFragment())
        }
    }

}