package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): Result<List<Country>> {
        return repository.getCountries()
    }
}