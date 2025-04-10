package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.repository.HomeRepository
import javax.inject.Inject

class ToggleLikeBusinessUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    /**
     * Executes the use case to toggle the liked state of a business.
     * @param businessId The ID of the business.
     * @return Result indicating success or failure.
     */
    suspend operator fun invoke(businessId: String): Result<Unit> {
        // Assuming repository method handles the toggle logic based on ID only.
        return repository.toggleLikeBusiness(businessId, true)
    }
}