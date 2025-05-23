package com.koru.capital.profile.data.repository

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.profile.data.mapper.toDomain
import com.koru.capital.profile.data.datasource.ProfileApiService
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMyProfile()
            val apiResponseBody = response.body()

            if (response.isSuccessful && apiResponseBody?.status == "success" && apiResponseBody.data?.user != null) {
                val profileDto = apiResponseBody.data.user!!
                Result.success(profileDto.toDomain())
            } else {
                val errorMsg = apiResponseBody?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching profile (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching profile: ${e.message}", e))
        }
    }

    override suspend fun updateMyProfile(
        firstName: RequestBody,
        lastName: RequestBody,
        biography: RequestBody?,
        linkedinProfile: RequestBody?,
        instagramHandle: RequestBody?,
        profileImage: MultipartBody.Part?
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateMyProfile(
                firstName = firstName,
                lastName = lastName,
                biography = biography,
                linkedinProfile = linkedinProfile,
                instagramHandle = instagramHandle,
                profileImage = profileImage
            )
            val apiResponseBody = response.body()

            if (response.isSuccessful && apiResponseBody?.status == "success" && apiResponseBody.data?.user != null) {
                val updatedProfileDto = apiResponseBody.data.user!!
                Result.success(updatedProfileDto.toDomain())
            } else {
                val errorMsg = apiResponseBody?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error updating profile (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error updating profile: ${e.message}", e))
        }
    }
}