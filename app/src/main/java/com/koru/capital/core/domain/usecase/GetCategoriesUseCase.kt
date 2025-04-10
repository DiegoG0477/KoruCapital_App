package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    /**
     * Obtiene todas las categorías de negocio.
     * @return Result con la lista de Categorías.
     */
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategories()
    }
}