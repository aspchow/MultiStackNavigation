package com.avinash.projectmultinav.fragments.precontent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.avinash.projectmultinav.databinding.DownloadDataFragmentBinding

import com.avinash.projectmultinav.datastore.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DownloadDataFragment : Fragment() {
    lateinit var binding: DownloadDataFragmentBinding

    private val appViewModel by activityViewModels<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DownloadDataFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            var i = 0
            while (i <= 100) {
                delay(20)
                withContext(Dispatchers.Main) {
                    binding.progressBar.progress = i
                }
                i++
            }
            withContext(Dispatchers.Main) {
                findNavController().navigate(DownloadDataFragmentDirections.actionWelcomeFragmentToAppContentFragment())
            }
            appViewModel.saveSigningCompleted()
        }

    }

}