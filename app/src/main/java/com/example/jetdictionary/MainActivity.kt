package com.example.jetdictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetdictionary.core.DataStoreManager
import com.example.jetdictionary.presenter.navigation.MainNavGraph
import com.example.jetdictionary.presenter.navigation.MainNavHost
import com.example.jetdictionary.presenter.navigation.Navigator
import com.example.jetdictionary.presenter.screen.setting.SettingsScreen
import com.example.jetdictionary.ui.theme.JetDictionaryTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager


    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val savedTheme = runBlocking { dataStoreManager.getAppTheme() }

        setContent {
            JetDictionaryTheme(savedTheme = savedTheme) {
                // A surface container using the 'background' color from the theme
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
//                        MainNavGraph(navigator = navigator)
                        MainNavHost(navigator = navigator)
                    }
                }

            }
        }
    }
}
