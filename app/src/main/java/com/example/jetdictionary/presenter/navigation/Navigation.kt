package com.example.jetdictionary.presenter.navigation

import android.os.Parcelable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.material.*
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
    const val LOGIN_ROUTE = "login"
    const val HOME_ROUTE = "home"
    const val REGISTER_ROUTE = "register"
    const val PROFILE_ROUTE = "profile"
    const val SETTING_ROUTE = "setting"
    const val APP_SCAFFOLD_ROUTE = "app_scaffold"
    const val CHAT_GROUP_ROUTE = "chat_group"


}

sealed class BottomBarScreen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object HomeScreen : BottomBarScreen(
        route = NavigationDestinations.HOME_ROUTE,
        title = "Home",
        icon = R.drawable.ic_home
    )

    object ProfileScreen :
        BottomBarScreen(
            route = NavigationDestinations.PROFILE_ROUTE,
            title = "Profile",
            icon = R.drawable.ic_chat
        )

    object SettingsScreen : BottomBarScreen(
        route = NavigationDestinations.SETTING_ROUTE,
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
        fun toMainScreen() = object : NavigationAction {

            override val destination: String =
                NavigationDestinations.APP_SCAFFOLD_ROUTE

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()

        }

        fun toRegisterScreen() = object : NavigationAction {
            override val destination: String
                get() = NavigationDestinations.REGISTER_ROUTE
        }
    }

    object HomeScreen {
        fun toChatGroupScreen() = object : NavigationAction {
            override val destination: String
                get() = NavigationDestinations.CHAT_GROUP_ROUTE
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
    navigator: Navigator
) {


    val navController = rememberNavController()
    val navBarNavController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.LOGIN_ROUTE,
    ) {
        composable(NavigationDestinations.LOGIN_ROUTE) {
            LoginScreen(
                loginViewModel = hiltViewModel(),
                onLogin = {
//                    navController.popBackStack()
                }
            )
        }

        composable(NavigationDestinations.REGISTER_ROUTE) {
            RegisterScreen(onBack = {
                navController.navigateUp()
            }, registerViewModel = hiltViewModel())
        }

        composable(NavigationDestinations.APP_SCAFFOLD_ROUTE) {
            AppScaffold(navigator = navigator, navController = navBarNavController)
        }
    }
}

@Composable
fun BottomBarNavHost(
    navController: NavController,
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
        navController = navController as NavHostController,
        startDestination = NavigationDestinations.HOME_ROUTE,
    ) {

        composable(route = NavigationDestinations.HOME_ROUTE) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
            )
        }

        composable(NavigationDestinations.PROFILE_ROUTE) {
            AddPostScreen()
        }

        composable(NavigationDestinations.SETTING_ROUTE) {
            NotificationScreen()
        }

        composable(NavigationDestinations.CHAT_GROUP_ROUTE) {
            ChatGroupScreen()
        }

    }


}


@Composable
fun MainNavGraph(
    startDestination: String = NavigationDestinations.LOGIN_ROUTE, navigator: Navigator
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )

    val items = listOf(
        BottomBarScreen.HomeScreen,
        BottomBarScreen.ProfileScreen,
        BottomBarScreen.SettingsScreen,
    )


    LaunchedEffect(navigatorState) {
        Log.e("AMBE1203", navigatorState?.destination.toString())

        navigatorState?.let { it ->
            // currentBackStackEntry?.arguments? == null 08/03/2022
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navController.navigate(it.destination, it.navOptions)
        }

    }
    Scaffold(
        bottomBar = {
            items.forEach { item ->
                if (item.route == currentRoute) {
                    BottomNavigation(
                        navController = navController,
                        items = items,
                        currentRoute = currentRoute
                    )
                }

            }

        },
        backgroundColor = MaterialTheme.colors.background,
        content = {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(NavigationDestinations.LOGIN_ROUTE) {
                    LoginScreen(
                        loginViewModel = hiltViewModel(),
                        onLogin = {
//                    navController.popBackStack()
                        }
                    )
                }

                composable(NavigationDestinations.REGISTER_ROUTE) {
                    RegisterScreen(onBack = {
                        navController.navigateUp()
                    }, registerViewModel = hiltViewModel())
                }

                composable(route = NavigationDestinations.HOME_ROUTE) {
                    HomeScreen(
                        homeViewModel = hiltViewModel(),
                    )
                }

                composable(NavigationDestinations.PROFILE_ROUTE) {
                    AddPostScreen()
                }

                composable(NavigationDestinations.SETTING_ROUTE) {
                    NotificationScreen()
                }

                composable(NavigationDestinations.CHAT_GROUP_ROUTE) {
                    ChatGroupScreen()
                }

//                composable(NavigationDestinations.APP_SCAFFOLD_ROUTE) {
//                    AppScaffold(navigator = navigator)
//                }
            }
        }
    )

}


@Composable
fun BottomNavigation(
    navController: NavController,
    items: List<BottomBarScreen>,
    currentRoute: String?

) {

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
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
                selected = currentRoute == item.route,
                onClick = {

                    if (currentRoute == item.route) {
                        return@BottomNavigationItem
                    }
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { router ->
                                popUpTo(router) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }

    }
}

