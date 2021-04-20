package com.avinash.projectmultinav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avinash.projectmultinav.databinding.BugListingFragmentBinding


class BugListingFragment : Fragment() {

    lateinit var binding: BugListingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BugListingFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goTobugDetailBtn.setOnClickListener {
            findNavController().navigate(BugListingFragmentDirections.actionGlobalBugDetailFragment())
        }
    }
}