package com.koru.capital.profile.data.datasource

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.profile.data.dto.UserDataWrapperDto
import com.koru.capital.profile.data.dto.UserProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApiService {
    @GET("users/me")
    suspend fun getMyProfile(): Response<ApiResponseDto<UserDataWrapperDto>>

    @Multipart
    @PUT("users/me")
    suspend fun updateMyProfile(
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("biography") biography: RequestBody?,
        @Part("linkedinProfile") linkedinProfile: RequestBody?,
        @Part("instagramHandle") instagramHandle: RequestBody?,
        @Part profileImage: MultipartBody.Part?
    ): Response<ApiResponseDto<UserDataWrapperDto>>
}