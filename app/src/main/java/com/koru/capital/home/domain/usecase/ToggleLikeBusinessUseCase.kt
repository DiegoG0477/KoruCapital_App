package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.repository.HomeRepository
import javax.inject.Inject

class ToggleLikeBusinessUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(businessId: String): Result<Unit> {
        return repository.toggleLikeBusiness(businessId, true)
    }
}