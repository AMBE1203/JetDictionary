package com.example.jetdictionary.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jetdictionary.core.Constants.ACCESS_TOKEN_KEY
import com.example.jetdictionary.core.Constants.THEME_AUTO
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.getDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_app")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.getDataStore

    suspend fun setAppTheme(appTheme: String) {
        dataStore.edit { p ->
            p[PreferencesKeys.APP_THEME] = appTheme
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { p ->
            p[PreferencesKeys.ACCESS_TOKEN] = token
        }
    }

    suspend fun getAppTheme(): String =
        dataStore.data.first()[PreferencesKeys.APP_THEME] ?: THEME_AUTO

    suspend fun getToken(): String =
        dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN] ?: ""

    private object PreferencesKeys {
        val APP_THEME = stringPreferencesKey("appTheme")
        val ACCESS_TOKEN = stringPreferencesKey(ACCESS_TOKEN_KEY)
    }

}