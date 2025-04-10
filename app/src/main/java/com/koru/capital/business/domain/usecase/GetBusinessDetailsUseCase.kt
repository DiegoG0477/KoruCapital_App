package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.repository.BusinessRepository
import javax.inject.Inject

class GetBusinessDetailsUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businessId: String): Result<Business> {
        return repository.getBusinessDetails(businessId)
    }
}