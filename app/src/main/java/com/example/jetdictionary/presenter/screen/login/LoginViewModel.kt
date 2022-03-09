package com.example.jetdictionary.presenter.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetdictionary.core.getViewStateFlowForNetworkCall
import com.example.jetdictionary.core.toJson
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.repository.ILoginRepository
import com.example.jetdictionary.domain.usecase.LoginUseCase
import com.example.jetdictionary.presenter.base.BaseViewState
import com.example.jetdictionary.presenter.navigation.NavigationActions
import com.example.jetdictionary.presenter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val navigator: Navigator,
    private val iLoginRepository: ILoginRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginViewState(isShowLoading = false, isError = null, isSuccess = null))

    val uiState: StateFlow<LoginViewState> = _uiState.asStateFlow()

    val isLogged get() = iLoginRepository.isLogged()

    fun hideDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isError = null)
            }
        }
    }

    fun navigateToRegister() {
        navigator.navigate(NavigationActions.LoginScreen.toRegisterScreen())
    }

    fun navigateToHome() {
        val dummyData = LoginResponse(
            access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImNAZ21haWwuY29tIiwiaWQiOiI2MjI3MDZiZDQ5YzFmMjQ2YWU4YTZiMGQiLCJmdWxsbmFtZSI6ImN1b25nZHQiLCJhdmF0YXIiOiIiLCJpYXQiOjE2NDY4MDE1NDYsImV4cCI6MTY3ODMzNzU0Nn0.m2QSyDQp6TudH8mMCWumwUVLaHn3LzWu_cZyfRZh5zM",
            username = "c@gmail.com",
            id = "622706bd49c1f246ae8a6b0d",
            fullname = "dummyData",
            avatar = null
        )
        var someStringArgument = dummyData.toJson()
        _uiState.value.isSuccess?.let {
            someStringArgument = it.toJson()
        }
        navigator.navigate(
            NavigationActions.LoginScreen.toHomeScreen(
                someStringArgument = someStringArgument!!, // dummy data
                loginResponse = _uiState.value.isSuccess ?: dummyData
            )
        )
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
                        is BaseViewState.RenderSuccess -> state.copy(isSuccess = result.output.data)
                    }
                }

            }
        }
    }
}