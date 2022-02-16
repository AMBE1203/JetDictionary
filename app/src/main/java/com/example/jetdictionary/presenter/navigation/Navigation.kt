package com.example.jetdictionary.presenter.navigation

import android.os.Parcelable
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
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.presenter.screen.login.LoginScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NavigationDestinations {
    const val LOGIN_ROUTER = "login"
    const val HOME_ROUTER = "home"

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
            someParcelableObject: LoginResponse
        ) = object : NavigationAction {
            override val destination: String = "${NavigationDestinations.HOME_ROUTER}/$someStringArgument"
            override val parcelableArguments: Map<String, LoginResponse>
                get() = mapOf("LoginResponse" to someParcelableObject)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()

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

    }
}
