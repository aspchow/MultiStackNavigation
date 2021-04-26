package com.avinash.projectmultinav.fragments.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avinash.Task
import com.avinash.projectmultinav.databinding.ModuleListItemBinding

import com.avinash.projectmultinav.databinding.TasksListingFragmentBinding
import com.avinash.projectmultinav.printMsg
import com.avinash.tasksListLiveData


class TasksListingFragment : Fragment() {

    private lateinit var binding: TasksListingFragmentBinding

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


        val taskListingAdapter = TaskListingAdapter {
            findNavController().navigate(TasksListingFragmentDirections.actionGlobalTaskDetailsFragment(taskId = it))
        }

        binding.tasksRecyclerView.apply {
            adapter = taskListingAdapter
        }

        tasksListLiveData.observe(viewLifecycleOwner) {
            printMsg("The list is ${it.size}")
            taskListingAdapter.submitList(it)
        }

    }

}


class TaskListingAdapter(val itemClickListener: (it : Int) -> Unit) : ListAdapter<Task, ModuleListViewHolder>(object : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleListViewHolder {
        return ModuleListViewHolder(ModuleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ModuleListViewHolder, position: Int) {
        printMsg("Inside the ")
        val task = getItem(position)
        holder.binding.apply {
            root.setOnClickListener {
                itemClickListener(task.taskId)
            }
            moduleName.text = task.taskName
            moduleDescription.text = task.taskDescription
        }
    }


}

class ModuleListViewHolder(val binding: ModuleListItemBinding) : RecyclerView.ViewHolder(binding.root)