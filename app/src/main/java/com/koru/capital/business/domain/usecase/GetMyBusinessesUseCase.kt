package com.koru.capital.business.domain.usecase

import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.business.presentation.viewmodel.BusinessFilter
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel
import javax.inject.Inject

class GetMyBusinessesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    /**
     * Obtiene la lista de negocios del usuario según el filtro.
     * @param filter El filtro a aplicar.
     * @return Result con la lista de BusinessListItemUiModel o error.
     */
    suspend operator fun invoke(filter: BusinessFilter): Result<List<BusinessListItemUiModel>> {
        // La autenticación (obtener userId) se asume manejada implícitamente
        // o debería pasarse como parámetro si fuera necesario.
        // Por ahora, el repositorio/API se encarga basado en el token.
        return repository.getMyBusinesses(filter)
    }
}