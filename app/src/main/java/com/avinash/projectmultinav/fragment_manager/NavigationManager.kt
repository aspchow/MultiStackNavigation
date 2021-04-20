package com.avinash.projectmultinav.fragment_manager

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.avinash.projectmultinav.R
import com.avinash.projectmultinav.printMsg
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationManager {

    lateinit var fragmentManager: FragmentManager
    private var tabHistory = mutableListOf<Pair<Int, Fragment>>()
    lateinit var currnetNavController: NavController
    lateinit var lastOpenedFragment: Fragment

    lateinit var bottomNavigationView: BottomNavigationView


    @SuppressLint("RestrictedApi")
    private fun handleTabClick(startDestinationId: Int) {

        printMsg("The is of the current clicked is $startDestinationId")
        if (::lastOpenedFragment.isInitialized)
            lastOpenedFragment.requireView().visibility = View.INVISIBLE

        // don't change the order in the adding the NavHost
        val fragment = NavHostFragment()

        fragmentManager
                .beginTransaction()
                .add(R.id.appContextLayout, fragment, startDestinationId.toString())
                .commitNow()

        fragment.navController.apply {
            graph = navInflater.inflate(R.navigation.app_content_navigation).apply {
                startDestination = startDestinationId
            }
        }

        lastOpenedFragment = fragment
        currnetNavController = fragment.navController
        tabHistory.add(Pair(startDestinationId, fragment))
    }


    @SuppressLint("RestrictedApi")
    fun onBackPressed(): Boolean {

        return if (::currnetNavController.isInitialized.not()) true
        else if (currnetNavController.backStack.size <= 2 && tabHistory.size == 1) {
            fragmentManager
                    .beginTransaction()
                    .remove(lastOpenedFragment)
                    .commit()
            true
        } else if (currnetNavController.popBackStack()) {
            false
        } else {

            fragmentManager
                    .beginTransaction()
                    .remove(tabHistory.removeLast().second)
                    .commit()

            val tabInfo = tabHistory.last()
            currnetNavController = tabInfo.second.findNavController()

            bottomNavigationView.menu.findItem(tabInfo.first).isChecked = true

            lastOpenedFragment = tabInfo.second
            lastOpenedFragment.requireView().visibility = View.VISIBLE
            false
        }
    }

    fun startNavigation(fm: FragmentManager, bottomNavigationView: BottomNavigationView) {


        this.bottomNavigationView = bottomNavigationView

        fm.fragments.forEach {
            val navHost = it as NavHostFragment
            navHost.navController.run {
                if (it.tag != null) {
                    graph = navInflater.inflate(R.navigation.app_content_navigation).apply {
                        startDestination = navHost.tag!!.toInt()
                    }
                }
            }
        }


        bottomNavigationView.menu.forEach {
            it.setOnMenuItemClickListener { menuItem ->
                printMsg("The Menu item is clicked ${it.title}")
                if (tabHistory.map { it.first }.contains(it.itemId).not())
                    handleTabClick(
                            it.itemId
                    )
                else {
                    lastOpenedFragment.requireView().visibility = View.INVISIBLE
                    val tabItem = tabHistory.find { it.first == menuItem.itemId }!!
                    lastOpenedFragment = tabItem.second
                    currnetNavController = tabItem.second.findNavController()
                    lastOpenedFragment.requireView().visibility = View.VISIBLE
                    printMsg("already existed")
                }
                false
            }
        }

        this.fragmentManager = fm

        if (tabHistory.isEmpty())
            handleTabClick(R.id.taskListing)
        else {
            tabHistory = tabHistory.map {
                Pair(it.first, fm.findFragmentByTag(it.first.toString()))
            } as MutableList<Pair<Int, Fragment>>
            // tabHistory.addAll(fragmentManager.fragments.filter { it.tag != null }.map { Pair(it.tag!!.toInt(), it) })
            currnetNavController = tabHistory.last().second.findNavController()
            lastOpenedFragment = tabHistory.last().second
            printMsg("The current navcontroller ${fragmentManager.fragments.map { it.tag }}")
        }
    }
}


