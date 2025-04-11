package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials): Result<AuthToken> {
        if (credentials.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches()) {
            return Result.failure(IllegalArgumentException("Formato de correo inválido"))
        }
        if (credentials.password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        return repository.login(credentials)
    }
}
