package com.example.jetdictionary.domain.repository

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.model.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface IRegisterRepository {
    suspend fun register(registerParam: RegisterParam): Flow<IOResult<RegisterResponse>>
}