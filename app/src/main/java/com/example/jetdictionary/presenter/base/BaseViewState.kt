package com.example.jetdictionary.presenter.base

sealed class BaseViewState<out T> {
    data class Loading(val isLoading: Boolean) : BaseViewState<Nothing>()

    data class RenderSuccess<out T >(val output: T) : BaseViewState<T>()

    data class RenderFailure(val throwable: Throwable) : BaseViewState<Nothing>()
}