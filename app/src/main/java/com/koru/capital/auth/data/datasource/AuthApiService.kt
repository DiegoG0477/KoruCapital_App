package com.koru.capital.auth.data.datasource

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.auth.data.dto.LoginRequestDto
import com.koru.capital.auth.data.dto.LoginResponseDto
import com.koru.capital.auth.data.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiResponseDto<LoginResponseDto>>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiResponseDto<LoginResponseDto>>
}