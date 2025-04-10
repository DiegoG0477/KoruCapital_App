package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.repository.HomeRepository
import javax.inject.Inject

class ToggleSaveBusinessUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    /**
     * Executes the use case to toggle the saved state of a business.
     * @param businessId The ID of the business.
     * @return Result indicating success or failure.
     */
    suspend operator fun invoke(businessId: String): Result<Unit> {
        // Assuming repository method handles the toggle logic based on ID only.
        // Pass a dummy boolean, or modify repository if it needs the current state.
        return repository.toggleSaveBusiness(businessId, true)
    }
}