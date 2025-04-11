package com.koru.capital.auth.domain.repository

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.model.RegistrationData

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Result<AuthToken>
    suspend fun register(registrationData: RegistrationData): Result<AuthToken>

    suspend fun logout(): Result<Unit>

}