package com.example.jetdictionary.data.repository

import com.example.jetdictionary.core.DataStoreManager
import com.example.jetdictionary.core.IOResult
import com.example.jetdictionary.core.NetworkHelper
import com.example.jetdictionary.core.performSafeNetworkApiCall
import com.example.jetdictionary.data.source.local.AppDatabase
import com.example.jetdictionary.data.source.local.ILocalSource
import com.example.jetdictionary.data.source.remote.IRemoteApi
import com.example.jetdictionary.domain.model.BaseResponse
import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val iRemoteApi: IRemoteApi,
    private val iLocalSource: ILocalSource,
    private val database: AppDatabase,
    private val networkHelper: NetworkHelper,
    private val dataStoreManager: DataStoreManager
) : ILoginRepository {
    override suspend fun login(loginParam: LoginParam): Flow<IOResult<BaseResponse<LoginResponse>>> =
        performSafeNetworkApiCall(networkHelper = networkHelper, networkApiCall = {
            iRemoteApi.login(loginParam = loginParam)
        }).onEach {
            if (it is IOResult.OnSuccess) {
                iLocalSource.saveAccessToken(it.data.data?.access_token ?: "")
                dataStoreManager.setToken(token = it.data.data?.access_token ?: "")
            }
        }

    override fun isLogged(): Boolean = iLocalSource.getAccessToken().isNotBlank()
}