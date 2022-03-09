package com.example.jetdictionary.domain.model

data class BaseResponse<T>(
    var statusCode: Int,
    var message: String,
    var data: T?,
)