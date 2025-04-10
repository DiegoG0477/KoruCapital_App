// capital/auth/data/repository/AuthRepositoryImpl.kt
package com.koru.capital.auth.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.koru.capital.auth.data.local.TokenStorage
// Import the generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Import mappers
import com.koru.capital.auth.data.mapper.toDomain
import com.koru.capital.auth.data.mapper.toRequestDto
// Import API service
import com.koru.capital.auth.data.datasource.AuthApiService
// Import domain models
import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.model.RegistrationData
// Import domain repository interface
import com.koru.capital.auth.domain.repository.AuthRepository
// Coroutines and DI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(credentials: LoginCredentials): Result<AuthToken> = withContext(Dispatchers.IO) {
        try {
            val requestDto = credentials.toRequestDto()
            // apiService.login now returns Response<ApiResponseDto<LoginResponseDto>>
            val response = apiService.login(requestDto)

            // Check HTTP success AND status within JSON body
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val loginResponseData = response.body()!!.data!! // data is LoginResponseDto
                val authToken = loginResponseData.toDomain() // Map LoginResponseDto to AuthToken

                if (authToken != null) {
                    tokenStorage.saveToken(authToken)
                    Result.success(authToken)
                } else {
                    // If toDomain() returns null (e.g., accessToken was null in DTO)
                    Result.failure(Exception("Login failed: Invalid token data received."))
                }
            } else {
                // Handle API error, HTTP error, or missing data
                val errorMsg = response.body()?.message // Use API message if available
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Login failed (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Login failed: ${e.message}", e))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun register(registrationData: RegistrationData): Result<AuthToken> = withContext(Dispatchers.IO) {
        try {
            val requestDto = registrationData.toRequestDto()
            // apiService.register now returns Response<ApiResponseDto<LoginResponseDto>>
            val response = apiService.register(requestDto)

            // Check HTTP success AND status within JSON body
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val registerResponseData = response.body()!!.data!! // data is LoginResponseDto
                val authToken = registerResponseData.toDomain() // Map LoginResponseDto to AuthToken

                if (authToken != null) {
                    tokenStorage.saveToken(authToken)
                    Result.success(authToken)
                } else {
                    // Registration might have succeeded on backend, but token is bad
                    Result.failure(Exception("Registration succeeded but failed to process token data."))
                }
            } else {
                // Handle API error, HTTP error, or missing data
                val errorMsg = response.body()?.message // Use API message if available
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Registration failed (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message}", e))
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            tokenStorage.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Logout failed while clearing local token: ${e.message}", e))
        }
    }
}