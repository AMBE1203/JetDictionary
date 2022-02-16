package com.example.jetdictionary.domain.usecase

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(private val repository: ILoginRepository): IUseCase<LoginParam, LoginResponse> {
    override suspend fun execute(input: LoginParam): Flow<IOResult<LoginResponse>> = repository.login(loginParam = input)
}