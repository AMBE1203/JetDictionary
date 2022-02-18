package com.example.jetdictionary.presenter.screen.home

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetdictionary.R
import com.example.jetdictionary.presenter.view.CustomToolbar
import com.example.jetdictionary.presenter.view.LoadingScreen
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@Composable
fun HomeScreen() {
    LoadingScreen(isLoading = false) {
        Scaffold(topBar = {
            CustomToolbar(title = stringResource(id = R.string.home), showBackButton = false)
        }, content = {
            Text(text = "Home")
        })
    }
}

@Preview
@Composable
fun HomePreview() {
    JetDictionaryTheme {

        HomeScreen()

    }
}