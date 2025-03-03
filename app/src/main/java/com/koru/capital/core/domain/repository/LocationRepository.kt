package com.koru.capital.core.domain.repository

import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State

interface LocationRepository {
    suspend fun getStates(): List<State>
    suspend fun getMunicipalities(stateId: String): List<Municipality>
    suspend fun getCategories(): List<Category>
}