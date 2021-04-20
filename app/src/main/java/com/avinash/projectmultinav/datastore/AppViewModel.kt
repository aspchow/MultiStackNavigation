package com.avinash.projectmultinav.datastore

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.avinash.projectmultinav.fragment_manager.NavigationManager
import com.avinash.projectmultinav.printMsg
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppViewModelFactory(
    private val context: Context,
    val navManager : NavigationManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(context, navManager) as T
    }

}


class AppViewModel(
    context: Context,
  val navManager : NavigationManager
) : ViewModel() {


    init {
        printMsg("The new viewmodel is created")
    }

    private val dataStorer = DataStorer(context)

    val isContentFragmentOpened = MutableLiveData(false)

    val isSigned = dataStorer.checkIsDownloadComplete().asLiveData(context = Dispatchers.IO)




    fun saveSigningCompleted() =
        viewModelScope.launch(Dispatchers.IO) { dataStorer.saveAsTheDownloadComplete() }


}