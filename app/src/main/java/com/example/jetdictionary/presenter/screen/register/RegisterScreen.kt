package com.example.jetdictionary.presenter.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetdictionary.R
import com.example.jetdictionary.presenter.base.TextFieldState
import com.example.jetdictionary.presenter.screen.login.ConfirmPasswordState
import com.example.jetdictionary.presenter.screen.login.EmailState
import com.example.jetdictionary.presenter.screen.login.PasswordState
import com.example.jetdictionary.presenter.view.CustomToolbar
import com.example.jetdictionary.presenter.view.InputEmail
import com.example.jetdictionary.presenter.view.InputPassword
import com.example.jetdictionary.presenter.view.supportWideScreen
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@Composable
fun RegisterScreen(onBack: () -> Unit) {
    val emailState = remember { EmailState() }
    val passwordState = remember { PasswordState() }
    val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }

    Scaffold(topBar = {
        CustomToolbar(title = stringResource(id = R.string.sign_up), onBackPressed = {
            onBack()
        })
    }, content = {
        LazyColumn(
            modifier = Modifier
                .supportWideScreen()
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            item {
                RegisterContent(
                    onRegisterSubmitted = { email, password -> },
                    emailState = emailState,
                    passwordState = passwordState,
                    confirmPasswordState = confirmPasswordState
                )

            }
        }
    })
}


@Composable
fun RegisterContent(
    onRegisterSubmitted: (email: String, password: String) -> Unit,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    confirmPasswordState: TextFieldState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusConfirmRequester = remember { FocusRequester() }

        InputEmail(emailState = emailState, onImeAction = { focusRequester.requestFocus() })
        Spacer(modifier = Modifier.height(16.dp))
        InputPassword(
            hint = stringResource(id = R.string.password), passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { focusRequester.requestFocus() },
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputPassword(
            hint = stringResource(id = R.string.confirm_password),
            passwordState = confirmPasswordState,
            modifier = Modifier.focusRequester(focusConfirmRequester),
            onImeAction = { onRegisterSubmitted(emailState.text, passwordState.text) },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onRegisterSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid && passwordState.isValid && confirmPasswordState.isValid
        ) {

            Text(text = stringResource(id = R.string.create_account))

        }


    }

}

@Preview
@Composable
fun RegisterPreview() {
    JetDictionaryTheme {
        RegisterScreen(onBack = {})
    }
}