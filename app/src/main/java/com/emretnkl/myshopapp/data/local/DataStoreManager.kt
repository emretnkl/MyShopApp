package com.emretnkl.myshopapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "user_settings")
class DataStoreManager(context : Context) {
    private object PreferencesKeys {
        val ONBOARDING_VISIBLE = booleanPreferencesKey("onboarding visible")
        val USERNAME = stringPreferencesKey("username")
    }

    private val dataStore = context.dataStore

    suspend fun setOnboardingVisible(isVisible: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.ONBOARDING_VISIBLE] = isVisible
        }
    }

    val getOnboardingVisible: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ONBOARDING_VISIBLE] ?: false
    }

    suspend fun setUsername(username : String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.USERNAME] = username
        }

    }
    val getUsername: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USERNAME] ?: ""
    }
}