package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.repository.BusinessRepository
import javax.inject.Inject

class DeleteBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businessId: String): Result<Unit> {
        return repository.deleteBusiness(businessId)
    }
}