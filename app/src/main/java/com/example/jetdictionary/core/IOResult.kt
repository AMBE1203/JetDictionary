package com.example.jetdictionary.core

sealed class IOResult<out T> {
    data class OnFailed(val error: Throwable): IOResult<Nothing>()
    data class OnSuccess<out T>(val data: T): IOResult<T>()
}