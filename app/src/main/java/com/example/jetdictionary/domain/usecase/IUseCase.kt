package com.example.jetdictionary.domain.usecase

import com.example.jetdictionary.core.IOResult
import kotlinx.coroutines.flow.Flow

interface IUseCase<in I, out O> {
    suspend fun execute(input: I): Flow<IOResult<O>>
}