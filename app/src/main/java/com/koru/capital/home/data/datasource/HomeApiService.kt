package com.koru.capital.home.data.datasource

// Import generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Specific DTOs for Home
import com.koru.capital.home.data.dto.PagedBusinessResponseDto
// Generic simple response DTO (assuming toggles don't return specific data)
import com.koru.capital.core.data.dto.ResponseDto // Can reuse this or use ApiResponseDto<Any?>

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface HomeApiService {

    @GET("businesses/feed")
    suspend fun getBusinesses(
        @QueryMap filters: Map<String, String>,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponseDto<PagedBusinessResponseDto>> // <-- Wrap PagedBusinessResponseDto

    @POST("businesses/{id}/save")
    suspend fun toggleSaveBusiness(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<Any?>> // <-- Wrap with generic Any? data or a specific simple DTO if API returns one

    @POST("businesses/{id}/like")
    suspend fun toggleLikeBusiness(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<Any?>> // <-- Wrap with generic Any? data or specific DTO
}