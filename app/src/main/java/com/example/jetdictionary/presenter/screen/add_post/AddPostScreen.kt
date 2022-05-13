package com.example.jetdictionary.presenter.screen.add_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.jetdictionary.R
import com.example.jetdictionary.core.Constants.THEME_DARK
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@Composable
fun AddPostScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Add Post Screen",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = androidx.compose.ui.graphics.Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
@Preview
fun AddPostScreenPreview() {
    JetDictionaryTheme(savedTheme = THEME_DARK) {
        AddPostScreen()
    }
}