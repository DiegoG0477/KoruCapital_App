package com.koru.capital.auth.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.koru.capital.auth.data.local.TokenStorage
import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.auth.data.mapper.toDomain
import com.koru.capital.auth.data.mapper.toRequestDto
import com.koru.capital.auth.data.datasource.AuthApiService
import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.model.RegistrationData
import com.koru.capital.auth.domain.repository.AuthRepository
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
            val response = apiService.login(requestDto)

            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val loginResponseData = response.body()!!.data!!
                val authToken = loginResponseData.toDomain()

                if (authToken != null) {
                    tokenStorage.saveToken(authToken)
                    Result.success(authToken)
                } else {
                    Result.failure(Exception("Login failed: Invalid token data received."))
                }
            } else {
                val errorMsg = response.body()?.message
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
            val response = apiService.register(requestDto)

            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val registerResponseData = response.body()!!.data!!
                val authToken = registerResponseData.toDomain()

                if (authToken != null) {
                    tokenStorage.saveToken(authToken)
                    Result.success(authToken)
                } else {
                    Result.failure(Exception("Registration succeeded but failed to process token data."))
                }
            } else {
                val errorMsg = response.body()?.message
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