// File: capital/auth/domain/usecase/RegisterUserUseCase.kt (New)
package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.RegistrationData
import com.koru.capital.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
    // Inject validators if needed
) {
    /**
     * Executes the user registration use case.
     * @param data The collected registration information.
     * @return Result containing AuthToken on success, or Exception on failure.
     */
    suspend operator fun invoke(data: RegistrationData): Result<AuthToken> {
        // Perform Domain-level validation if necessary (beyond basic ViewModel checks)
        // Example: Check if birthDate indicates user is old enough
        // if (data.birthDate.plusYears(18).isAfter(LocalDate.now())) {
        //    return Result.failure(IllegalArgumentException("Debes ser mayor de 18 años."))
        // }

        // Password complexity validation could also live here or in data layer depending on strategy

        return repository.register(data)
    }
}