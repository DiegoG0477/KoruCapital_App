package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    /**
     * Obtiene la lista de todos los países.
     * @return Result con la lista de Countries.
     */
    suspend operator fun invoke(): Result<List<Country>> {
        return repository.getCountries()
    }
}