package com.example.jetdictionary.presenter.screen.login

import com.example.jetdictionary.core.getViewStateFlowForNetworkCall
import com.example.jetdictionary.domain.model.BaseResponse
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.repository.ILoginRepository
import com.example.jetdictionary.domain.usecase.LoginUseCase
import com.example.jetdictionary.presenter.base.BaseViewModel
import com.example.jetdictionary.presenter.navigation.NavigationActions
import com.example.jetdictionary.presenter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val navigator: Navigator,
    private val iLoginRepository: ILoginRepository
) : BaseViewModel<BaseResponse<LoginResponse>, Throwable>() {

    val isLogged get() = iLoginRepository.isLogged()

    fun navigateToRegister() {
        navigator.navigate(NavigationActions.LoginScreen.toRegisterScreen())
    }

    fun navigateToHome() {
        navigator.navigate(
            NavigationActions.LoginScreen.toHomeScreen()
        )
    }

    fun hideDialog() {
        updateState(currentState().removeError())
    }


    fun login(email: String, password: String) {
        asyncFlow {
            getViewStateFlowForNetworkCall {
                loginUseCase.execute(LoginParam(email, password))
            }.collect { result ->
                updateState(newState = result)

            }
        }
    }
}