package com.example.jetdictionary.presenter.screen.home

import androidx.lifecycle.ViewModel
import com.example.jetdictionary.data.source.local.ILocalSource
import com.example.jetdictionary.presenter.navigation.NavigationActions
import com.example.jetdictionary.presenter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val iLocalSource: ILocalSource,
    private val navigator: Navigator,
) : ViewModel() {

    init {


    }

    fun toChatGroupScreen() {
        navigator.navigate(NavigationActions.HomeScreen.toChatGroupScreen())

    }
}