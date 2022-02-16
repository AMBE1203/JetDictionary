package com.example.jetdictionary.presenter.screen.login

import com.example.jetdictionary.core.Errors
import com.example.jetdictionary.domain.model.LoginResponse

data class LoginViewState(
    val isShowLoading: Boolean = false,
    val isError: Throwable?,
    val isSuccess: LoginResponse?
)