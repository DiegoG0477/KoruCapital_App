package com.koru.capital.core.data.repository

import com.koru.capital.core.data.mapper.toDomain
import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.repository.LocationRepository
import com.koru.capital.core.data.remote.LocationApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiService: LocationApiService
) : LocationRepository {

    override suspend fun getCountries(): Result<List<Country>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCountries()
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!!
                Result.success(dtoList.map { it.toDomain() })
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching countries (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching countries: ${e.message}", e))
        }
    }

    override suspend fun getStatesByCountry(countryId: String): Result<List<State>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getStatesByCountry(countryId)
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!!
                Result.success(dtoList.map { it.toDomain() })
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching states for country $countryId (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching states: ${e.message}", e))
        }
    }

    override suspend fun getMunicipalitiesByState(stateId: String): Result<List<Municipality>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMunicipalitiesByState(stateId)
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!!
                Result.success(dtoList.map { it.toDomain() })
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching municipalities for state $stateId (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching municipalities: ${e.message}", e))
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCategories()
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!!
                Result.success(dtoList.map { it.toDomain() })
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching categories (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching categories: ${e.message}", e))
        }
    }
}