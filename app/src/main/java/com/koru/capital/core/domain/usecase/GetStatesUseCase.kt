package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    /**
     * Obtiene los estados para un país específico.
     * @param countryId El ID del país (ej: "MX"). Si está vacío, devuelve lista vacía.
     * @return Result con la lista de Estados para ese país.
     */
    suspend operator fun invoke(countryId: String): Result<List<State>> {
        if (countryId.isBlank()) {
            // Devolver éxito con lista vacía si no se proporciona countryId
            return Result.success(emptyList())
        }
        return repository.getStatesByCountry(countryId)
    }
}