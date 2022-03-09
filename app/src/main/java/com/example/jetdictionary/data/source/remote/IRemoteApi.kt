package com.example.jetdictionary.data.source.remote

import com.example.jetdictionary.domain.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface IRemoteApi {
    @POST("/sign-in")
    suspend fun login(@Body loginParam: LoginParam?): Response<BaseResponse<LoginResponse>>

    @POST("/sign-up")
    suspend fun register(@Body registerParam: RegisterParam): Response<BaseResponse<RegisterResponse>>
}