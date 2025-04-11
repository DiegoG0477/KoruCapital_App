package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.repository.HomeRepository
import javax.inject.Inject

class ToggleSaveBusinessUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(businessId: String): Result<Unit> {
        return repository.toggleSaveBusiness(businessId, true)
    }
}