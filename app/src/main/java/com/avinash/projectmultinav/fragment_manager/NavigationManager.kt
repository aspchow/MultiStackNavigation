package com.avinash.projectmultinav.fragment_manager

import android.annotation.SuppressLint
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.avinash.projectmultinav.R
import com.avinash.projectmultinav.fragments.content.BugListingFragmentDirections
import com.avinash.projectmultinav.fragments.content.TasksListingFragmentDirections
import com.avinash.projectmultinav.printMsg
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception


object DeepLinkConst {
    val Task_Module = "task"
    val Bug_Module = "bug"
}

class NavigationManager {

    lateinit var fragmentManager: FragmentManager
    private var tabHistory = mutableListOf<Pair<Int, Fragment>>()
    private lateinit var currnetNavController: NavController
    private lateinit var lastOpenedFragment: Fragment
    private var toolbar: Toolbar? = null

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


            bottomNavigationView.menu.findItem(tabInfo.first).apply {
                isChecked = true
                toolbar?.changeToolBarValuesWith(this)
            }

            lastOpenedFragment = tabInfo.second
            lastOpenedFragment.requireView().visibility = View.VISIBLE
            false
        }
    }


    fun navigateToDefaultFragmement() {
        val menuItem = bottomNavigationView.menu[0]
        handleTabClick(menuItem.itemId)
        toolbar?.changeToolBarValuesWith(menuItem)
    }


    fun startNavigation(fm: FragmentManager, bottomNavigationView: BottomNavigationView, uri: Uri? = null, toolbar: Toolbar? = null) {


        printMsg("The uri is ${uri.toString()}")
        this.bottomNavigationView = bottomNavigationView
        this.toolbar = toolbar

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

                toolbar?.changeToolBarValuesWith(menuItem)

                val itemInTabHistory = tabHistory.find { it.first == menuItem.itemId }
                if (itemInTabHistory == null) {
                    handleTabClick(it.itemId)
                } else {
                    tabHistory.remove(itemInTabHistory)
                    tabHistory.add(itemInTabHistory)
                    lastOpenedFragment.requireView().visibility = View.INVISIBLE
                    val tabItem = tabHistory.find { it.first == menuItem.itemId }!!
                    lastOpenedFragment = tabItem.second
                    currnetNavController = tabItem.second.findNavController()
                    lastOpenedFragment.requireView().visibility = View.VISIBLE
                    printMsg("already existed")
                }
                printMsg("The tab history is $tabHistory")

                false
            }
        }

        this.fragmentManager = fm

        if (tabHistory.isEmpty()) {

            printMsg("The uri is ${uri?.getQueryParameter("module")}  ${uri?.getQueryParameter("id")}")

            if (uri != null) {
                val module = uri.getQueryParameter("module")
                if (module != null) {
                    when (module) {
                        DeepLinkConst.Task_Module -> {
                            printMsg("The In the task module")
                            handleTabClick(R.id.taskListing)
                            currnetNavController.navigate(TasksListingFragmentDirections.actionGlobalTaskDetailsFragment(taskId = uri.getQueryParameter("id")?.safeInt
                                    ?: -1))
                            val menuItem = bottomNavigationView.menu.findItem(R.id.taskListing)
                            menuItem.apply {
                                isChecked = true
                                toolbar?.changeToolBarValuesWith(this)
                            }
                        }

                        DeepLinkConst.Bug_Module -> {
                            printMsg("The In the bug module")
                            handleTabClick(R.id.bugListing)
                            currnetNavController.navigate(BugListingFragmentDirections.actionGlobalBugDetailFragment(bugId = uri.getQueryParameter("id")?.safeInt
                                    ?: -1))
                            val menuItem = bottomNavigationView.menu.findItem(R.id.bugListing)
                            menuItem.apply {
                                isChecked = true
                                toolbar?.changeToolBarValuesWith(this)
                            }
                        }
                        else -> {
                            Toast.makeText(bottomNavigationView.context, "SomeThing Has gone Wrong with link", Toast.LENGTH_SHORT).show()
                            navigateToDefaultFragmement()
                        }
                    }
                } else {
                    Toast.makeText(bottomNavigationView.context, "SomeThing Has gone Wrong with link", Toast.LENGTH_SHORT).show()
                    navigateToDefaultFragmement()
                }

            } else {
                navigateToDefaultFragmement()
            }


            /*  val menuItem = bottomNavigationView.menu[0]
              handleTabClick(menuItem.itemId)
              toolbar?.changeToolBarValuesWith(menuItem)*/
        } else {

            tabHistory = tabHistory.map {
                val fragment = fm.findFragmentByTag(it.first.toString())!!

                fragment.requireView().visibility = View.INVISIBLE
                Pair(it.first, fragment)
            } as MutableList<Pair<Int, Fragment>>

            printMsg("The tabhistory is ${tabHistory.map { it.second.tag }}")

            currnetNavController = tabHistory.last().second.findNavController()
            lastOpenedFragment = tabHistory.last().second
            lastOpenedFragment.requireView().visibility = View.VISIBLE

            val menuItem = bottomNavigationView.menu.findItem(lastOpenedFragment.tag!!.toInt())

            menuItem.isChecked = true

            toolbar?.changeToolBarValuesWith(menuItem)

        }
    }
}


fun Toolbar.changeToolBarValuesWith(menuItem: MenuItem) {
    title = menuItem.title
    navigationIcon = menuItem.icon
}

val String.safeInt: Int
    get() = try {
        this.toInt()
    } catch (e: Exception) {
        -1
    }