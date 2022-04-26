package com.example.jetdictionary.presenter.navigation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetdictionary.R
import com.example.jetdictionary.presenter.screen.add_post.AddPostScreen
import com.example.jetdictionary.presenter.screen.chat_group.ChatGroupScreen
import com.example.jetdictionary.presenter.screen.home.HomeScreen
import com.example.jetdictionary.presenter.screen.login.LoginScreen
import com.example.jetdictionary.presenter.screen.main.AppScaffold
import com.example.jetdictionary.presenter.screen.notification.NotificationScreen
import com.example.jetdictionary.presenter.screen.register.RegisterScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NavigationDestinations {
    const val LOGIN_ROUTER = "login"
    const val HOME_ROUTER = "home"
    const val REGISTER_ROUTER = "register"
    const val PROFILE_ROUTER = "profile"
    const val SETTING_ROUTER = "setting"
    const val APP_SCAFFOLD_ROUTER = "app_scaffold"
    const val CHAT_GROUP_ROUTER = "chat_group"

}

sealed class BottomBarScreen(val router: String, val title: String, @DrawableRes val icon: Int) {
    object HomeScreen : BottomBarScreen(
        router = NavigationDestinations.HOME_ROUTER,
        title = "Home",
        icon = R.drawable.ic_home
    )

    object ProfileScreen :
        BottomBarScreen(
            router = NavigationDestinations.PROFILE_ROUTER,
            title = "Profile",
            icon = R.drawable.ic_chat
        )

    object SettingsScreen : BottomBarScreen(
        router = NavigationDestinations.SETTING_ROUTER,
        title = "Settings",
        icon = R.drawable.ic_settings
    )
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
        fun toHomeScreen() = object : NavigationAction {

            override val destination: String =
                NavigationDestinations.APP_SCAFFOLD_ROUTER

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

    object HomeScreen{
        fun toChatGroupScreen() = object  : NavigationAction {
            override val destination: String
                get() = NavigationDestinations.CHAT_GROUP_ROUTER
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
    navBarNavController: NavController = rememberNavController(),
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
                t = hiltViewModel(),
                onLogin = {
//                    navController.popBackStack()
                }
            )
        }

        composable(NavigationDestinations.REGISTER_ROUTER) {
            RegisterScreen(onBack = {
                navController.navigateUp()
            }, registerViewModel = hiltViewModel())
        }

        composable(NavigationDestinations.APP_SCAFFOLD_ROUTER) {
            AppScaffold(navController = navBarNavController, navigator = navigator)
        }


    }
}

@Composable
fun BottomBarNavHost(
    navController: NavHostController,
    navigator: Navigator
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )

    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.HOME_ROUTER
    ) {

        composable(route = NavigationDestinations.HOME_ROUTER) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
            )
        }

        composable(NavigationDestinations.PROFILE_ROUTER) {
            AddPostScreen()
        }

        composable(NavigationDestinations.SETTING_ROUTER) {
            NotificationScreen()
        }

        composable(NavigationDestinations.CHAT_GROUP_ROUTER){
            ChatGroupScreen()
        }

    }

        LaunchedEffect(navigatorState) {
        navigatorState?.let { it ->
            // currentBackStackEntry?.arguments? == null 08/03/2022
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navController.navigate(it.destination, it.navOptions)
        }
    }
}


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomBarScreen.HomeScreen,
        BottomBarScreen.ProfileScreen,
        BottomBarScreen.SettingsScreen,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRouter = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(label = {
                Text(text = item.title, fontSize = 9.sp)
            },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRouter == item.router,
                onClick = {
                    navController.navigate(item.router) {
                        navController.graph.startDestinationRoute?.let { router ->
                            popUpTo(router) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }

    }
}

