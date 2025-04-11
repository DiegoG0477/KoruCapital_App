package com.koru.capital.core.data.remote

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.CountryDto
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {

    @GET("locations/countries")
    suspend fun getCountries(): Response<ApiResponseDto<List<CountryDto>>>

    @GET("locations/states")
    suspend fun getStatesByCountry(
        @Query("countryId") countryId: String
    ): Response<ApiResponseDto<List<StateDto>>>

    @GET("locations/municipalities")
    suspend fun getMunicipalitiesByState(
        @Query("stateId") stateId: String
    ): Response<ApiResponseDto<List<MunicipalityDto>>>

    @GET("locations/categories")
    suspend fun getCategories(): Response<ApiResponseDto<List<CategoryDto>>>
}