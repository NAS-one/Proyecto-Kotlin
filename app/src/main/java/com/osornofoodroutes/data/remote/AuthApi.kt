package com.osornofoodroutes.data.remote

import com.osornofoodroutes.data.remote.dto.LoginRequest
import com.osornofoodroutes.data.remote.dto.MessageResponse
import com.osornofoodroutes.data.remote.dto.RegisterRequest
import com.osornofoodroutes.data.remote.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): MessageResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse
}
