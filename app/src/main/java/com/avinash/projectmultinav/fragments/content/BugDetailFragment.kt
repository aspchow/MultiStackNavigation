package com.avinash.projectmultinav.fragments.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avinash.bugListLiveData
import com.avinash.projectmultinav.R
import com.avinash.projectmultinav.databinding.BugDetailFragmentBinding

class BugDetailFragment : Fragment() {

    private lateinit var binding: BugDetailFragmentBinding

    val args by navArgs<BugDetailFragmentArgs>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = BugDetailFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            bugListLiveData
                    .map {
                        it.find {
                            it.bugId == args.bugId
                        }
                    }
                    .observe(viewLifecycleOwner) {


                        val adapter = SubTasksAdapter {
                            findNavController()
                                    .navigate(
                                            BugDetailFragmentDirections
                                                    .actionGlobalTaskDetailsFragment(taskId = it))
                        }

                        linkedTasks.adapter = adapter


                        if (it != null) {
                            bugName.text = it.bugName
                            bugDescription.text = it.bugDescription


                            adapter.submitList(it.linkedTasks)

                        }
                    }
        }

    }


}