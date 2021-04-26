package com.avinash.projectmultinav.fragments.precontent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.avinash.projectmultinav.databinding.SignInFragmentBinding
import com.avinash.projectmultinav.datastore.AppViewModel

class SignInFragment : Fragment() {

    lateinit var binding: SignInFragmentBinding

    private val appViewModel by activityViewModels<AppViewModel>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        appViewModel.isSigned.observe(viewLifecycleOwner, {
            if (it)
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToAppContentFragment())
        })



        binding = SignInFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToWelcomeFragment())
        }
    }

}