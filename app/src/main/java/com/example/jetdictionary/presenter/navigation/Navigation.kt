package com.example.jetdictionary.presenter.navigation

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetdictionary.core.fromJson
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.presenter.screen.home.HomeScreen
import com.example.jetdictionary.presenter.screen.login.LoginScreen
import com.example.jetdictionary.presenter.screen.register.RegisterScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NavigationDestinations {
    const val LOGIN_ROUTER = "login"
    const val HOME_ROUTER = "home"
    const val REGISTER_ROUTER = "register"

}

interface NavigationAction {
    val destination: String
    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}

object NavigationActions {
    object LoginScreen {
        fun toHomeScreen(
            someStringArgument: String,
            loginResponse: LoginResponse
        ) = object : NavigationAction {

            override val destination: String =
                "${NavigationDestinations.HOME_ROUTER}/$someStringArgument"
            override val parcelableArguments: Map<String, Parcelable>
                get() = mapOf("LoginResponse" to loginResponse)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()

        }

        fun toRegisterScreen() = object : NavigationAction {
            override val destination: String
                get() = NavigationDestinations.REGISTER_ROUTER
        }
    }
}

interface Navigator {

    val navActions: StateFlow<NavigationAction?>

    fun navigate(navAction: NavigationAction?)
}

class ComposeCustomNavigator : Navigator {

    private val _navActions: MutableStateFlow<NavigationAction?> by lazy {
        MutableStateFlow(null)
    }

    override val navActions = _navActions.asStateFlow()

    override fun navigate(navAction: NavigationAction?) {
        _navActions.update { navAction }
    }

}

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
    navigator: Navigator
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    LaunchedEffect(navigatorState) {
        navigatorState?.let { it ->
            // currentBackStackEntry?.arguments? == null 08/03/2022
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navController.navigate(it.destination, it.navOptions)
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.LOGIN_ROUTER
    ) {
        composable(NavigationDestinations.LOGIN_ROUTER) {
            LoginScreen(
                loginViewModel = hiltViewModel(),
            )
        }

        composable(NavigationDestinations.REGISTER_ROUTER) {
            RegisterScreen(onBack = {
                navController.navigateUp()
            }, registerViewModel = hiltViewModel())
        }

        composable(
            route = "${NavigationDestinations.HOME_ROUTER}/{someStringArgument}",
            arguments = listOf(
                navArgument("someStringArgument") { type = NavType.StringType })
        ) {
            val someStringArgument = it.arguments?.getString("someStringArgument")
            val loginResponse =
                navController.previousBackStackEntry?.arguments?.getParcelable<LoginResponse>("LoginResponse")
                    ?: someStringArgument?.fromJson(type = LoginResponse::class.java)
            Log.e("AMBE1203", someStringArgument.toString())
            HomeScreen(
                homeViewModel = hiltViewModel(),
                someStringArgument = someStringArgument,
                loginResponse = loginResponse
            )
        }

    }
}
