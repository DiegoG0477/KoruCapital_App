// capital/core/data/repository/LocationRepositoryImpl.kt
package com.koru.capital.core.data.repository

import com.koru.capital.core.data.mapper.toDomain
// Import the generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Domain Models
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
// Domain Repository Interface
import com.koru.capital.core.domain.repository.LocationRepository
// API Service
import com.koru.capital.core.data.remote.LocationApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiService: LocationApiService
) : LocationRepository {

    override suspend fun getCountries(): Result<List<Country>> = withContext(Dispatchers.IO) {
        try {
            // Now returns Response<ApiResponseDto<List<CountryDto>>>
            val response = apiService.getCountries()
            // Check HTTP success AND status field AND data field
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!! // data is List<CountryDto>
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
            // Now returns Response<ApiResponseDto<List<StateDto>>>
            val response = apiService.getStatesByCountry(countryId)
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!! // data is List<StateDto>
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
            // Now returns Response<ApiResponseDto<List<MunicipalityDto>>>
            val response = apiService.getMunicipalitiesByState(stateId)
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!! // data is List<MunicipalityDto>
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
            // Now returns Response<ApiResponseDto<List<CategoryDto>>>
            val response = apiService.getCategories()
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!! // data is List<CategoryDto>
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