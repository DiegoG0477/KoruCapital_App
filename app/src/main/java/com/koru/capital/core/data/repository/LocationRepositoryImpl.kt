package com.koru.capital.core.data.repository

import com.koru.capital.core.data.mapper.toDomain
import com.koru.capital.core.data.remote.LocationApiService
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiService: LocationApiService
) : LocationRepository {

    override suspend fun getStates(): List<State> {
        return apiService.getStates().map { it.toDomain() }
    }

    override suspend fun getMunicipalities(stateId: String): List<Municipality> {
        return apiService.getMunicipalities(stateId).map { it.toDomain() }
    }

    override suspend fun getCategories(): List<Category> {
        return apiService.getCategories().map { it.toDomain() }
    }
}
