package com.avinash.projectmultinav.fragments.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avinash.Bug
import com.avinash.Task
import com.avinash.bugListLiveData
import com.avinash.projectmultinav.databinding.BugListingFragmentBinding
import com.avinash.projectmultinav.databinding.ModuleListItemBinding
import com.avinash.projectmultinav.printMsg


class BugListingFragment : Fragment() {

    lateinit var binding: BugListingFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = BugListingFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            val adapter = BugListingAdapter {
                findNavController().navigate(BugListingFragmentDirections.actionGlobalBugDetailFragment(bugId = it))
            }


            bugsListingRecyclerView.adapter = adapter


            bugListLiveData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        }
    }
}


class BugListingAdapter(val itemClickListener: (it : Int) -> Unit) : ListAdapter<Bug, ModuleListViewHolder>(object : DiffUtil.ItemCallback<Bug>() {
    override fun areItemsTheSame(oldItem: Bug, newItem: Bug): Boolean {
        return oldItem.bugId == newItem.bugId
    }

    override fun areContentsTheSame(oldItem: Bug, newItem: Bug): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleListViewHolder {
        return ModuleListViewHolder(ModuleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ModuleListViewHolder, position: Int) {
        val task = getItem(position)
        holder.binding.apply {
            root.setOnClickListener {
                itemClickListener(task.bugId)
            }
            moduleName.text = task.bugName
            moduleDescription.text = task.bugDescription
        }
    }


}
