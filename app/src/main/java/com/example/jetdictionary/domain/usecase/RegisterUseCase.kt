package com.example.jetdictionary.domain.usecase

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.domain.model.BaseResponse
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.model.RegisterResponse
import com.example.jetdictionary.domain.repository.IRegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(private val iRegisterRepository: IRegisterRepository) :
    IUseCase<RegisterParam, BaseResponse<RegisterResponse>> {
    override suspend fun execute(input: RegisterParam): Flow<IOResult<BaseResponse<RegisterResponse>>> =
        iRegisterRepository.register(registerParam = input)
}