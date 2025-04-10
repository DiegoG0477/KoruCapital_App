// capital/business/data/datasource/BusinessApiService.kt
package com.koru.capital.business.data.datasource

// Import generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Specific DTOs for Business
import com.koru.capital.business.data.dto.BusinessDto
import com.koru.capital.business.data.dto.BusinessListItemDto

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface BusinessApiService {

    @Multipart
    @POST("businesses")
    suspend fun addBusiness(
        @Part("ownerId") ownerId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("investment") investment: RequestBody,
        @Part("profitPercentage") profitPercentage: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("municipalityId") municipalityId: RequestBody,
        @Part("businessModel") businessModel: RequestBody,
        @Part("monthlyIncome") monthlyIncome: RequestBody,
        @Part imageUrl: MultipartBody.Part?
    ): Response<ApiResponseDto<BusinessDto>> // <-- Wrap BusinessDto

    @GET("businesses/{id}")
    suspend fun getBusinessDetails(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<BusinessDto>> // <-- Wrap BusinessDto

    @Multipart
    @PUT("businesses/{id}")
    suspend fun updateBusiness(
        @Path("id") businessId: String,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("investment") investment: RequestBody?,
        @Part("profitPercentage") profitPercentage: RequestBody?,
        @Part("categoryId") categoryId: RequestBody?,
        @Part("municipalityId") municipalityId: RequestBody?,
        @Part("businessModel") businessModel: RequestBody?,
        @Part("monthlyIncome") monthlyIncome: RequestBody?,
        @Part imageUrl: MultipartBody.Part?
    ): Response<ApiResponseDto<BusinessDto>> // <-- Wrap BusinessDto

    @GET("businesses/mine")
    suspend fun getMyBusinesses(
        @Query("filter") filter: String // "OWNED", "SAVED", "PARTNERED"
    ): Response<ApiResponseDto<List<BusinessListItemDto>>> // <-- Wrap List

    @DELETE("businesses/{id}")
    suspend fun deleteBusiness(
        @Path("id") businessId: String
    ): Response<ApiResponseDto<Any?>> // <-- Wrap with generic data
}