package com.example.jetdictionary.data.source.remote

import com.example.jetdictionary.domain.model.LoginParam
import com.example.jetdictionary.domain.model.LoginResponse
import com.example.jetdictionary.domain.model.RegisterParam
import com.example.jetdictionary.domain.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface IRemoteApi {
    @POST("/login")
    suspend fun login(@Body loginParam: LoginParam?): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerParam: RegisterParam): Response<RegisterResponse>
}