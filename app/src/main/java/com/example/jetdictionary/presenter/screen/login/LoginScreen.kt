package com.example.jetdictionary.presenter.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetdictionary.R
import com.example.jetdictionary.presenter.base.TextFieldState
import com.example.jetdictionary.presenter.view.*
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val showDialog = uiState.isError != null
    val keyboardController = LocalSoftwareKeyboardController.current


    val emailState = remember { EmailState() }
    val passwordState = remember { PasswordState() }
    if (showDialog) {
        ShowAlertDialog(
            onDismiss = { loginViewModel.hideDialog() },
            message = uiState.isError?.message ?: "Unknown error"
        )
    }
    LoadingScreen(isLoading = uiState.isShowLoading) {
        Scaffold(content = {
            LazyColumn(
                modifier = Modifier
                    .supportWideScreen()
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)

            ) {
                item {

                    Text(
                        text = "Welcome to JetDictionary",
                        style = MaterialTheme.typography.caption,

                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    LoginContent(
                        onLoginSubmitted = { email, password ->
                            keyboardController?.hide()
                            loginViewModel.login(email, password)
                        },
                        emailState = emailState,
                        passwordState = passwordState
                    )
                    TextButton(
                        onClick = {
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.forgot_password))
                    }

                    TextButton(
                        onClick = {
                            loginViewModel.navigateToRegister()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.register))
                    }

                }
            }
        })
    }

}

@Composable
fun LoginContent(
    onLoginSubmitted: (email: String, password: String) -> Unit,
    emailState: TextFieldState,
    passwordState: TextFieldState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        InputEmail(emailState = emailState, onImeAction = { focusRequester.requestFocus() })
        Spacer(modifier = Modifier.height(16.dp))
        InputPassword(hint = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = {
                onLoginSubmitted(emailState.text, passwordState.text)
            })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLoginSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(text = stringResource(id = R.string.sign_in))

        }
    }

}


@Preview
@Composable
fun LoginPreview() {
    JetDictionaryTheme {
        LoginScreen(loginViewModel = hiltViewModel())
    }
}