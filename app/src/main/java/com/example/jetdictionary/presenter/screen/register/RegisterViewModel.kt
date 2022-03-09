package com.example.jetdictionary.presenter.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetdictionary.core.getViewStateFlowForNetworkCall
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.usecase.RegisterUseCase
import com.example.jetdictionary.presenter.base.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(RegisterViewState(isShowLoading = false, isError = null, isSuccess = null))

    val uiState = _uiState.asStateFlow()

    fun hideDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isError = null)
            }
        }
    }

    fun register(email: String, password: String, fullname: String, avatar: String?) {
        viewModelScope.launch {
            getViewStateFlowForNetworkCall {
                registerUseCase.execute(RegisterParam(username = email, password = password, fullname = fullname, avatar = avatar ?: ""))
            }.collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is BaseViewState.Loading -> state.copy(isShowLoading = result.isLoading)
                        is BaseViewState.RenderFailure -> state.copy(isError = result.throwable)
                        is BaseViewState.RenderSuccess -> state.copy(isSuccess = result.output)
                    }
                }

            }
        }
    }

}