package com.example.jetdictionary.domain.model

data class BaseResponse<T>(
    var code: Int,
    var message: String,
    var data: T?,
)