package com.example.jetdictionary.domain.repository

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.domain.model.BaseResponse
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {
    suspend fun login(loginParam: LoginParam): Flow<IOResult<BaseResponse<LoginResponse>>>

    fun isLogged() : Boolean
}