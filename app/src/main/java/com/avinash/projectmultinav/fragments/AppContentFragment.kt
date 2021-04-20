package com.avinash.projectmultinav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.avinash.projectmultinav.R
import com.avinash.projectmultinav.databinding.AppContentFragmentBinding
import com.avinash.projectmultinav.datastore.AppViewModel


class AppContentFragment : Fragment() {

    lateinit var binding: AppContentFragmentBinding

    val appViewModel by activityViewModels<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AppContentFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.isContentFragmentOpened.value = true

    }


}


fun AppContentFragment.addTheNavHost(fragment: Fragment) {
    parentFragmentManager
        .beginTransaction()
        .add(
            R.id.appContextLayout,
            fragment
        )
        .setPrimaryNavigationFragment(fragment)
        .commit()
}