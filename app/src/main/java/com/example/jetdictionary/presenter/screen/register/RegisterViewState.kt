package com.example.jetdictionary.presenter.screen.register

import com.example.jetdictionary.domain.model.RegisterResponse

data class RegisterViewState(
    val isShowLoading: Boolean = false,
    val isError: Throwable?,
    val isSuccess: RegisterResponse?
)