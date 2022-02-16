package com.example.jetdictionary.core

sealed class Errors: Throwable() {
    data class NetworkError(val statusCode: Int = -1, override val message: String = "") : Errors()
    object EmptyInputError : Errors()
    object EmptyResultsError : Errors()
    object SingleError : Errors()
    data class NoInternetError(override val message: String = "No Internet") : Errors()
}