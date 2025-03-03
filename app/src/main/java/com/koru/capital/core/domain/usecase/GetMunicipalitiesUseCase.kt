package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetMunicipalitiesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(stateId: String): List<Municipality> {
        return repository.getMunicipalities(stateId)
    }
}