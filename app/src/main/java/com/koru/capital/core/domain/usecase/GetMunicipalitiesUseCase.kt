package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetMunicipalitiesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(stateId: String): Result<List<Municipality>> {
        if (stateId.isBlank()) {
            return Result.success(emptyList())
        }
        return repository.getMunicipalitiesByState(stateId)
    }
}