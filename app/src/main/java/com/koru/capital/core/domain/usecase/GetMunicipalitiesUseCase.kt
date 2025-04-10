package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetMunicipalitiesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    /**
     * Obtiene los municipios para un estado específico.
     * @param stateId El ID del estado (ej: "MX-JAL"). Si está vacío, devuelve lista vacía.
     * @return Result con la lista de Municipios para ese estado.
     */
    suspend operator fun invoke(stateId: String): Result<List<Municipality>> {
        if (stateId.isBlank()) {
            // Devolver éxito con lista vacía si no se proporciona stateId
            return Result.success(emptyList())
        }
        return repository.getMunicipalitiesByState(stateId)
    }
}