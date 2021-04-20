package com.avinash.projectmultinav

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.avinash.projectmultinav.databinding.ActivityMainBinding
import com.avinash.projectmultinav.datastore.AppViewModel
import com.avinash.projectmultinav.datastore.AppViewModelFactory
import com.avinash.projectmultinav.fragment_manager.NavigationManager


// for debugging

fun printMsg(msg: String) = Log.d("Avinash", msg)


class MainActivity : AppCompatActivity() {

    lateinit var bining: ActivityMainBinding
    val viewModel by viewModels<AppViewModel> {
        AppViewModelFactory(
                applicationContext,
                NavigationManager()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        printMsg("The hash of activity is ${this.hashCode()}")
        binding {

            setContentView(root)


            bottomNavigationView.menu.apply {
                add(1, R.id.taskListing, 1, "Tasks").apply {
                    icon = resources.getDrawable(R.drawable.ic_launcher_background)
                }

                add(1, R.id.bugListing, 1, "Bugs").apply {
                    icon = resources.getDrawable(R.drawable.ic_launcher_background)
                }

            }


            viewModel.isContentFragmentOpened.observe(this@MainActivity, {
                val visibility = if (it) {
                    val uri = intent.data
                    Toast.makeText(applicationContext, "the uri is $uri", Toast.LENGTH_SHORT).show()

                    viewModel.navManager.startNavigation(
                            supportFragmentManager,
                            bottomNavigationView
                    )
                    View.VISIBLE
                } else View.GONE
                toolbar.visibility = visibility
                bottomNavigationView.visibility = visibility
            })

        }
    }


    override fun onBackPressed() {
        if (viewModel.navManager.onBackPressed())
            super.onBackPressed()
    }

}


fun MainActivity.addActivityManager() {
    //NavigationManager(bining.bottomNavigationView)
}


fun MainActivity.binding(lambda: ActivityMainBinding.() -> Unit) {
    ActivityMainBinding.inflate(layoutInflater).apply {
        bining = this
        lambda()
    }
}

fun MainActivity.viewModel(lambda: AppViewModel.() -> Unit) {
    viewModel.apply(lambda)
}