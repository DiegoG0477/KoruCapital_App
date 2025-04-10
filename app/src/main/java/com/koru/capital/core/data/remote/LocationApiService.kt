// capital/core/data/remote/LocationApiService.kt
package com.koru.capital.core.data.remote

// Import the generic wrapper and specific List DTOs
import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.CountryDto
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
// Remove ListResponse DTO imports if they only contained 'data'
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {

    @GET("locations/countries")
    suspend fun getCountries(): Response<ApiResponseDto<List<CountryDto>>> // <-- Wrap List

    @GET("locations/states")
    suspend fun getStatesByCountry(
        @Query("countryId") countryId: String
    ): Response<ApiResponseDto<List<StateDto>>> // <-- Wrap List

    @GET("locations/municipalities")
    suspend fun getMunicipalitiesByState(
        @Query("stateId") stateId: String
    ): Response<ApiResponseDto<List<MunicipalityDto>>> // <-- Wrap List

    @GET("locations/categories")
    suspend fun getCategories(): Response<ApiResponseDto<List<CategoryDto>>> // <-- Wrap List
}