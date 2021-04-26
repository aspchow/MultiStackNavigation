package com.avinash.projectmultinav.fragments.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avinash.projectmultinav.databinding.SingleTextDisplayBinding
import com.avinash.projectmultinav.databinding.TaskDetailsFragmentBinding
import com.avinash.projectmultinav.printMsg
import com.avinash.tasksListLiveData


class TaskDetailsFragment : Fragment() {

    lateinit var binding: TaskDetailsFragmentBinding
    private val args by navArgs<TaskDetailsFragmentArgs>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        printMsg("Inside the oncreate view ${args.taskId}")
        binding = TaskDetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val taskListingAdapter = SubTasksAdapter {
            findNavController()
                    .navigate(TaskDetailsFragmentDirections.actionGlobalTaskDetailsFragment(taskId = it))
        }

        binding.apply {

            subTaskRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskListingAdapter
            }


            tasksListLiveData.map { tasks ->
                tasks.find { task ->
                    task.taskId == args.taskId
                }
            }.observe(viewLifecycleOwner) { task ->

                if (task != null) {
                    taskName.text = task.taskName
                    taskDescription.text = task.taskDescription

                    taskListingAdapter.submitList(task.subTasks)

                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        printMsg("On destroy is called ")
    }
}


class SubTasksAdapter(val onClickListener: (id: Int) -> Unit = {}) : ListAdapter<Int, SubTasksViewHolder>(object : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTasksViewHolder {
        return SubTasksViewHolder(SingleTextDisplayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubTasksViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            header.text = "Task ${item}"
            root.setOnClickListener {
                onClickListener(item)
            }
        }
    }

}


class SubTasksViewHolder(val binding: SingleTextDisplayBinding) : RecyclerView.ViewHolder(binding.root)