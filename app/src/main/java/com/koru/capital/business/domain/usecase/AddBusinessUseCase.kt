package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.Business
import com.koru.capital.business.domain.BusinessRepository
import javax.inject.Inject

class AddBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(business: Business): Result<Unit> {
        return repository.addBusiness(business)
    }
}
