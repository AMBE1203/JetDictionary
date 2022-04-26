package com.example.jetdictionary.presenter.screen.register

import com.example.jetdictionary.core.getViewStateFlowForNetworkCall
import com.example.jetdictionary.domain.model.BaseResponse
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.model.RegisterResponse
import com.example.jetdictionary.domain.usecase.RegisterUseCase
import com.example.jetdictionary.presenter.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<BaseResponse<RegisterResponse>, Throwable>() {


    fun hideErrorDialog() {
        currentState().removeError()
    }

    fun register(email: String, password: String, fullName: String, avatar: String?) {

        asyncFlow {
            getViewStateFlowForNetworkCall {
                registerUseCase.execute(
                    RegisterParam(
                        username = email,
                        password = password,
                        fullname = fullName,
                        avatar = avatar ?: ""
                    )
                )
            }.collect { result ->
                updateState(newState = result)
            }
        }

    }

}