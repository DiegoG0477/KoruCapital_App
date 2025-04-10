package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.data.local.TokenStorage // Usar TokenStorage
import javax.inject.Inject

// Simple UseCase para verificar si existe un token válido
class CheckAuthStatusUseCase @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    /**
     * Checks if a valid authentication token exists.
     * @return true if logged in, false otherwise.
     */
    suspend operator fun invoke(): Boolean {
        // getToken() ya incluye la lógica de validez/expiración en DataStoreTokenStorage
        return tokenStorage.getToken() != null
    }
}