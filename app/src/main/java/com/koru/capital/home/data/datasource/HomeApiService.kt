package com.koru.capital.home.data.datasource

import com.koru.capital.home.data.dto.BusinessFeedApiResponseDto

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.core.data.dto.ResponseDto

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
    ): Response<BusinessFeedApiResponseDto>

    @POST("businesses/{id}/save")
    suspend fun toggleSaveBusiness(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<Any?>>

    @POST("businesses/{id}/like")
    suspend fun toggleLikeBusiness(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<Any?>>
}