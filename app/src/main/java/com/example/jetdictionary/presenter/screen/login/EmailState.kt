package com.example.jetdictionary.presenter.screen.login

import com.example.jetdictionary.presenter.base.TextFieldState
import java.util.regex.Pattern

private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState : TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

private fun emailValidationError(email: String): String {
    return "invalid email: $email"
}

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}