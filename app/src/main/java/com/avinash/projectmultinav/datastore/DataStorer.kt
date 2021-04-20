package com.avinash.projectmultinav.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorer(context: Context) {
    val IS_SIGNED = "signed"
    val dataStore: DataStore<Preferences> = context.createDataStore(name = "isSigned")
    val dataStoreKey = preferencesKey<Boolean>(IS_SIGNED)

    suspend fun saveAsTheDownloadComplete() {

        dataStore.edit { preferences ->
            preferences[dataStoreKey] = true
        }
    }


    fun checkIsDownloadComplete(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: false
        }

    }
}