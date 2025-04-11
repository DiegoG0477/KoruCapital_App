package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(countryId: String): Result<List<State>> {
        if (countryId.isBlank()) {
            return Result.success(emptyList())
        }
        return repository.getStatesByCountry(countryId)
    }
}