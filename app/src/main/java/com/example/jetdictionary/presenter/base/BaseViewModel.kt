package com.example.jetdictionary.presenter.base

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<C, E> : ViewModel(), DefaultLifecycleObserver {

    companion object {
        var dispatcher: CoroutineDispatcher = Dispatchers.Default
    }

    private var _state: MutableStateFlow<ViewModelState<C, E>> =
        MutableStateFlow(ViewModelState.LoadingState())

    val state: StateFlow<ViewModelState<C, E>> get() = _state.asStateFlow()

    fun currentState(): ViewModelState<C, E> = _state.value

    protected fun updateState(newState: ViewModelState<C, E>) {

        _state.value = newState
    }


    protected inline fun async(crossinline block: suspend () -> Unit) =
        viewModelScope.launch {
            block()
        }

    protected suspend inline fun <T> await(crossinline block: suspend () -> T): T =
        withContext(dispatcher) { block() }

    protected inline fun asyncFlow(crossinline block: suspend () -> Unit) = async {
        withContext(dispatcher) {
            block()
        }
    }

}

sealed class ViewModelState<C, out E> {
    companion object {
        val initialState: ViewModelState<Nothing, Nothing> = LoadingState()
    }

    data class LoadingState<C, E>(val refreshing: Boolean = false) : ViewModelState<C, E>()

    data class ErrorState<C, E>(val error: E) : ViewModelState<C, E>()

    data class LoadedState<C, E>(
        val content: C,
        val error: E? = null,
        val refreshing: Boolean = false
    ) : ViewModelState<C, E>()


    fun loading(): Boolean = this is LoadingState && this.refreshing

    fun refreshingContent(): Boolean = when (this) {
        is LoadingState -> this.refreshing
        is LoadingState<*, *> -> this.refreshing
        else -> false
    }

    fun containsAnyError(): Boolean = error() != null

    fun content(): C? = when (this) {
        is LoadedState -> this.content
        else -> null
    }

    fun withContentIfLoaded(newContent: (C) -> C): ViewModelState<C, E> = when (this) {
        is LoadedState -> LoadedState(
            content = newContent(this.content),
            error = this.error,
            refreshing = this.refreshing
        )
        is LoadingState -> this
        is ErrorState -> this
    }

    fun error(): E? = when (this) {
        is ErrorState -> this.error
        is LoadedState -> this.error
        is LoadingState -> null
    }

    fun hasContent(): Boolean = content() != null

    fun markAsRefreshing(refreshing: Boolean = true): ViewModelState<C, E> = when (this) {
        is LoadingState -> LoadingState(refreshing)
        is ErrorState -> if (refreshing) LoadingState(refreshing) else this
        is LoadedState -> copy(refreshing = refreshing)
    }

    fun <E> withError(error: E): ViewModelState<C, E> = when (this) {
        is LoadingState -> ErrorState(error)
        is ErrorState -> ErrorState(error)
        is LoadedState -> LoadedState(
            error = error,
            content = this.content,
            refreshing = this.refreshing
        )
    }

    fun removeError(): ViewModelState<C, E> = when (this) {
        is LoadingState -> this
        is ErrorState -> LoadingState()
        is LoadedState -> LoadedState(
            content = this.content,
            refreshing = this.refreshing,
            error = null
        )
    }
}