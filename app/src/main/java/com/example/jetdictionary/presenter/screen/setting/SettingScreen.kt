package com.example.jetdictionary.presenter.screen.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetdictionary.BuildConfig
import com.example.jetdictionary.MainActivity
import com.example.jetdictionary.R
import com.example.jetdictionary.core.Constants

@Composable
fun SettingsScreen(
    settingViewModel: SettingViewModel
) {

    val uiState by settingViewModel.state.collectAsState()
    val context = LocalContext.current

//    LaunchedEffect(Unit){
//        (context as MainActivity).recreate()
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        content = {
            Content(
                isDark = uiState.content()?.isDark ?: false,
                settingViewModel = settingViewModel
            )
        },
        backgroundColor = MaterialTheme.colors.background
    )

}

@Composable
private fun Content(
    isDark: Boolean = false,
    settingViewModel: SettingViewModel

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .border(
                    border = BorderStroke(width = 1.dp, color = Color.LightGray),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.settings_dark_theme),
                    style = MaterialTheme.typography.body2
                )

                Switch(
                    checked = isDark,
                    onCheckedChange = {
                        if (it) {
                            settingViewModel.setAppTheme(Constants.THEME_DARK)

                        } else {
                            settingViewModel.setAppTheme(Constants.THEME_LIGHT)
                        }

                    })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.settings_screen_app_version_title),
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = BuildConfig.VERSION_NAME,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
