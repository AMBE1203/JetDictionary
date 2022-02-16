package com.example.jetdictionary.data.source.local

import android.content.SharedPreferences
import com.example.jetdictionary.core.Constants.ACCESS_TOKEN_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSourceImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ILocalSource {
    override fun getAccessToken(): String = sharedPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""

    override fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }
}