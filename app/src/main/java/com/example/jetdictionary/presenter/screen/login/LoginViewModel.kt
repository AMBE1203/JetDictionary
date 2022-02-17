package com.example.jetdictionary.presenter.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetdictionary.core.Errors
import com.example.jetdictionary.core.getViewStateFlowForNetworkCall
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.usecase.LoginUseCase
import com.example.jetdictionary.presenter.base.BaseViewState
import com.example.jetdictionary.presenter.navigation.NavigationActions
import com.example.jetdictionary.presenter.navigation.Navigator
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginViewState(isShowLoading = false, isError = null, isSuccess = null))

    val uiState: StateFlow<LoginViewState> = _uiState.asStateFlow()

    fun hideDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isError = null)
            }
        }
    }

    fun navigateToRegister(){
        navigator.navigate(NavigationActions.LoginScreen.toRegisterScreen())
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            getViewStateFlowForNetworkCall {
                loginUseCase.execute(
                    LoginParam(
                        email,
                        password
                    )
                )
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