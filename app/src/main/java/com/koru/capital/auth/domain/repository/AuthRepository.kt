package com.koru.capital.auth.domain.repository

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.model.RegistrationData

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Result<AuthToken>
    suspend fun register(registrationData: RegistrationData): Result<AuthToken>

    /**
     * Clears the stored authentication token.
     * @return Result indicating success or failure.
     */
    suspend fun logout(): Result<Unit> // <-- AÑADIR ESTO

    // suspend fun refreshToken(): Result<AuthToken>
    // suspend fun isLoggedIn(): Boolean // Podríamos añadir esto para el chequeo de sesión
}