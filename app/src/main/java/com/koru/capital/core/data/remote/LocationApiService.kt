package com.koru.capital.core.data.remote

import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("locations/states")
    suspend fun getStates(): List<StateDto>

    @GET("locations/municipalities")
    suspend fun getMunicipalities(
        @Query("stateId") stateId: String
    ): List<MunicipalityDto>

    @GET("locations/categories")
    suspend fun getCategories(): List<CategoryDto>
}