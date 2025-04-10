package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.repository.BusinessRepository
import javax.inject.Inject

class DeleteBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    /**
     * Ejecuta la eliminación de un negocio.
     * @param businessId ID del negocio a eliminar.
     * @return Result Unit en éxito o Exception en fallo.
     */
    suspend operator fun invoke(businessId: String): Result<Unit> {
        // La autorización (verificar dueño) se asume manejada por el repositorio/API
        return repository.deleteBusiness(businessId)
    }
}