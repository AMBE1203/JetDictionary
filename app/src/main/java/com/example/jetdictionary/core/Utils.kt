package com.example.jetdictionary.core

import com.example.jetdictionary.presenter.base.BaseViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import retrofit2.Response
import java.io.IOException

typealias NetworkAPIInvoke<T> = suspend () -> Response<T>


suspend fun <T> performSafeNetworkApiCall(
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    networkApiCall: NetworkAPIInvoke<T>,
    networkHelper: NetworkHelper
): Flow<IOResult<T>> {
    var delayDuration = 1000L
    val delayFactor = 2
    return flow {
        if (!networkHelper.isNetworkConnected()) {
            emit(IOResult.OnFailed(Errors.NoInternetError()))
            return@flow
        }
        val response = networkApiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(IOResult.OnSuccess(it))
            }
                ?: emit(IOResult.OnFailed(Errors.NetworkError(message = "API call successful but empty response body")))
            return@flow
        }
        emit(
            IOResult.OnFailed(
                Errors.NetworkError(
                    statusCode = response.code(),
                    message = response.errorBody()?.string() ?: messageInCaseOfError
                )
            )
        )
        return@flow
    }.catch { e ->
        emit(IOResult.OnFailed(Errors.NetworkError(message = "Exception during network API call: ${e.message}")))
        return@catch
    }.retryWhen { cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}

suspend fun <T : Any> getViewStateFlowForNetworkCall(ioOperation: suspend () -> Flow<IOResult<T>>) =
    flow {
        emit(BaseViewState.Loading(isLoading = true))
        ioOperation().map {
            when (it) {
                is IOResult.OnSuccess -> BaseViewState.RenderSuccess(it.data)
                is IOResult.OnFailed -> BaseViewState.RenderFailure(it.error)
            }
        }.collect {
            emit(it)
        }
        emit(BaseViewState.Loading(false))

    }.flowOn(Dispatchers.IO)