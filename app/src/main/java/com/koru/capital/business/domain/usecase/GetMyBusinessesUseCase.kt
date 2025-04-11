package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.business.presentation.viewmodel.BusinessFilter
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel
import javax.inject.Inject

class GetMyBusinessesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(filter: BusinessFilter): Result<List<BusinessListItemUiModel>> {
        return repository.getMyBusinesses(filter)
    }
}