package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
    // Inject validation logic if needed, or handle basic validation here/ViewModel
) {
    /**
     * Executes the login use case.
     * @param credentials User's login input.
     * @return Result containing AuthToken on success, or Exception on failure.
     */
    suspend operator fun invoke(credentials: LoginCredentials): Result<AuthToken> {
        // Basic validation (can be expanded or moved to a dedicated validator)
        if (credentials.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches()) {
            return Result.failure(IllegalArgumentException("Formato de correo inválido"))
        }
        if (credentials.password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }
        // Potentially add password length check if desired here too

        // Call the repository to perform the login
        return repository.login(credentials)
        // Post-login actions (like saving the token) might happen in the ViewModel
        // or triggered by observing the result of this use case.
    }
}
