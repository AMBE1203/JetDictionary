package com.example.jetdictionary.data.source.local

import javax.inject.Singleton

@Singleton
interface ILocalSource {
    fun getAccessToken(): String
    fun saveAccessToken(token: String)
}