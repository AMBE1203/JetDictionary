package com.example.jetdictionary.data.repository

import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.core.NetworkHelper
import com.example.jetdictionary.core.performSafeNetworkApiCall
import com.example.jetdictionary.data.source.local.AppDatabase
import com.example.jetdictionary.data.source.remote.IRemoteApi
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val iRemoteApi: IRemoteApi,
    private val database: AppDatabase,
    private val networkHelper: NetworkHelper
) : ILoginRepository {
    override suspend fun login(loginParam: LoginParam): Flow<IOResult<LoginResponse>> =
        performSafeNetworkApiCall(networkHelper = networkHelper, networkApiCall = {
            iRemoteApi.login(loginParam = loginParam)
        })
}