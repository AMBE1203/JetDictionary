package com.example.jetdictionary.domain.model

data class RegisterParam(
    val username: String,
    val password: String,
    val fullname: String,
    val avatar: String
)
