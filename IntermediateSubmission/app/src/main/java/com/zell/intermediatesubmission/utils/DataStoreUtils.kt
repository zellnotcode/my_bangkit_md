package com.zell.intermediatesubmission.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val USER_SESSION = "user_session"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SESSION)