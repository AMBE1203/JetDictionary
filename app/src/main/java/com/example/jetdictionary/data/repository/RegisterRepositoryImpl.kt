package com.example.jetdictionary.data.repository

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.core.NetworkHelper
import com.example.jetdictionary.core.performSafeNetworkApiCall
import com.example.jetdictionary.data.source.remote.IRemoteApi
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.model.RegisterResponse
import com.example.jetdictionary.domain.repository.IRegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepositoryImpl @Inject constructor(
    private val iRemoteApi: IRemoteApi,
    private val networkHelper: NetworkHelper
) : IRegisterRepository {
    override suspend fun register(registerParam: RegisterParam): Flow<IOResult<RegisterResponse>> =
        performSafeNetworkApiCall(networkHelper = networkHelper, networkApiCall = {
            iRemoteApi.register(registerParam)
        })
}