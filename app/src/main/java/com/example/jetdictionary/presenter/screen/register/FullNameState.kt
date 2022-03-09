package com.example.jetdictionary.presenter.screen.register

import com.example.jetdictionary.presenter.base.TextFieldState

class FullNameState : TextFieldState(validator = ::isFullNameValid, errorFor = ::fullNameValidationError)

private fun isFullNameValid(fullName: String): Boolean {
    return fullName.isNotEmpty()
}

private fun fullNameValidationError(error: String): String {
    return "FullName not blank"
}