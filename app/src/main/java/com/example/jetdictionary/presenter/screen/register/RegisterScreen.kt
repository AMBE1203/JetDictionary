package com.example.jetdictionary.presenter.screen.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetdictionary.R
import com.example.jetdictionary.core.Constants
import com.example.jetdictionary.presenter.base.TextFieldState
import com.example.jetdictionary.presenter.screen.login.ConfirmPasswordState
import com.example.jetdictionary.presenter.screen.login.EmailState
import com.example.jetdictionary.presenter.screen.login.PasswordState
import com.example.jetdictionary.presenter.view.*
import com.example.jetdictionary.ui.theme.JetDictionaryTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(onBack: () -> Unit, registerViewModel: RegisterViewModel) {
    val emailState = remember { EmailState() }
    val passwordState = remember { PasswordState() }
    val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
    val fullNameState = remember { FullNameState() }
    val uiState by registerViewModel.state.collectAsState()
    val showDialog = uiState.containsAnyError()
    val keyboardController = LocalSoftwareKeyboardController.current
    if (showDialog) {
        ShowAlertDialog(
            onDismiss = { registerViewModel.hideErrorDialog() },
            message = uiState.error()?.message ?: "Unknown error"
        )
    }
    if (uiState.hasContent()) {
        onBack()
    } else {
        LoadingScreen(isLoading = uiState.loading()) {
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
                            onRegisterSubmitted = { email, password, fullName, avatar ->
                                keyboardController?.hide()
                                registerViewModel.register(email, password, fullName, avatar)
                            },
                            emailState = emailState,
                            passwordState = passwordState,
                            confirmPasswordState = confirmPasswordState,
                            fullNameState = fullNameState
                        )

                    }
                }
            })
        }
    }
}


@Composable
fun RegisterContent(
    onRegisterSubmitted: (email: String, password: String, fullname: String, avatar: String?) -> Unit,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    confirmPasswordState: TextFieldState,
    fullNameState: TextFieldState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusConfirmRequester = remember { FocusRequester() }
        InputFullName(
            fullNameState = fullNameState,
            onImeAction = { focusRequester.requestFocus() })
        Spacer(modifier = Modifier.height(16.dp))

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
            onImeAction = {
                onRegisterSubmitted(
                    emailState.text,
                    passwordState.text,
                    fullNameState.text,
                    ""
                )
            },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onRegisterSubmitted(
                    emailState.text,
                    passwordState.text,
                    fullNameState.text,
                    ""
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid && passwordState.isValid && confirmPasswordState.isValid
        ) {

            Text(text = stringResource(id = R.string.create_account))

        }


    }

}

@Composable
fun InputFullName(
    fullNameState: TextFieldState,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}

) {
    TextField(
        value = fullNameState.text,
        onValueChange = {
            fullNameState.text = it
        },

        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                fullNameState.onFocusChange(focusState.isFocused)
                if (focusState.isFocused) {
                    fullNameState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
        isError = fullNameState.showErrors(),
        placeholder = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.full_name),
                    style = MaterialTheme.typography.body2
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
    fullNameState.getError()?.let {
        TextFieldError(textError = it)
    }
}

@Preview
@Composable
fun RegisterPreview() {
    JetDictionaryTheme(savedTheme = Constants.THEME_DARK) {
        RegisterScreen(
            onBack = {},
            registerViewModel = hiltViewModel()
        )
    }
}