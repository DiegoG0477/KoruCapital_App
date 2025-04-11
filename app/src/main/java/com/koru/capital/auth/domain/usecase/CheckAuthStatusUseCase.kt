package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.data.local.TokenStorage
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    suspend operator fun invoke(): Boolean {
        return tokenStorage.getToken() != null
    }
}