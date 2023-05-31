package com.zell.intermediatesubmission.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_SESSION_KEY = mapOf(
        "username" to stringPreferencesKey("username"),
        "email" to stringPreferencesKey("email"),
        "token" to stringPreferencesKey("token")
    )

    suspend fun saveData(data: Map<String, String>) {
        dataStore.edit { preferences ->
            data.forEach { (key, value) ->
                val dataStoreKey = USER_SESSION_KEY[key] ?: throw IllegalArgumentException("Invalid key")
                preferences[dataStoreKey] = value
            }
        }
    }

    fun getData(key: String): Flow<String> {
        val dataStoreKey = USER_SESSION_KEY[key] ?: throw IllegalArgumentException("Invalid key")
        return dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: ""
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}