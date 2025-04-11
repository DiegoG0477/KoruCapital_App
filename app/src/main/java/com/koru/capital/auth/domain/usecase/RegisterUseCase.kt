package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.RegistrationData
import com.koru.capital.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(data: RegistrationData): Result<AuthToken> {


        return repository.register(data)
    }
}