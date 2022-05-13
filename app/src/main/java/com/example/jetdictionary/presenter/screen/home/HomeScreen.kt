package com.example.jetdictionary.presenter.screen.home

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetdictionary.R
import com.example.jetdictionary.core.Constants.THEME_DARK
import com.example.jetdictionary.presenter.view.CustomToolbar
import com.example.jetdictionary.presenter.view.LoadingScreen
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
) {
    LoadingScreen(isLoading = false) {
        Scaffold(
            topBar = {
                CustomToolbar(title = stringResource(id = R.string.home), showBackButton = false)
            },
        ) {
            Button(onClick = {
                homeViewModel.toChatGroupScreen()
            }) {
                Text(text = "Click me")
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    JetDictionaryTheme(savedTheme = THEME_DARK) {

        HomeScreen(homeViewModel = hiltViewModel())

    }
}