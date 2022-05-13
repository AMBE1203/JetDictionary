package com.example.jetdictionary.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.jetdictionary.core.Constants.THEME_DARK
import com.example.jetdictionary.core.Constants.THEME_LIGHT

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val Colors.snackbarAction: Color
    @Composable
    get() = if (isLight) Purple300 else Purple700

@Composable
fun JetDictionaryTheme(
    savedTheme: String,
    content: @Composable () -> Unit
) {
    val colors: Colors
    when (savedTheme) {
        THEME_LIGHT -> {
            colors = LightColorPalette
        }
        THEME_DARK -> {
            colors = DarkColorPalette
        }
        else -> {
            if (isSystemInDarkTheme()) {
                colors = DarkColorPalette
            } else {
                colors = LightColorPalette
            }
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}