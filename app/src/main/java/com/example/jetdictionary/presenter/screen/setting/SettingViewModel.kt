package com.example.jetdictionary.presenter.screen.setting

import androidx.appcompat.app.AppCompatDelegate
import com.example.jetdictionary.core.Constants.THEME_DARK
import com.example.jetdictionary.core.Constants.THEME_LIGHT
import com.example.jetdictionary.core.DataStoreManager
import com.example.jetdictionary.presenter.base.BaseViewModel
import com.example.jetdictionary.presenter.base.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : BaseViewModel<SettingViewState, Throwable>() {


    fun setAppTheme(appTheme: String) {
        val mode = when (appTheme) {
            THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
        async {
            dataStoreManager.setAppTheme(appTheme = appTheme)
            val isDarkMode = when (appTheme) {
                THEME_LIGHT -> false
                THEME_DARK -> true
                else -> false
            }
            updateState(ViewModelState.LoadedState(content = SettingViewState(isDark = isDarkMode)))
        }

    }

}