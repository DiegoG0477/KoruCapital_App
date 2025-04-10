// capital/profile/data/repository/ProfileRepositoryImpl.kt
package com.koru.capital.profile.data.repository

// Import generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Mapper and API Service
import com.koru.capital.profile.data.mapper.toDomain
import com.koru.capital.profile.data.datasource.ProfileApiService
// Domain Model and Repository Interface
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
            // Now returns Response<ApiResponseDto<UserProfileDto>>
            val response = apiService.getMyProfile()
            // Check HTTP success AND status field AND data field
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val profileDto = response.body()!!.data!! // data is UserProfileDto
                Result.success(profileDto.toDomain())
            } else {
                val errorMsg = response.body()?.message
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
            // Now returns Response<ApiResponseDto<UserProfileDto>>
            val response = apiService.updateMyProfile(
                firstName = firstName,
                lastName = lastName,
                biography = biography,
                linkedinProfile = linkedinProfile,
                instagramHandle = instagramHandle,
                profileImage = profileImage
            )

            // Check HTTP success AND status field AND data field
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val updatedProfileDto = response.body()!!.data!! // data is UserProfileDto
                Result.success(updatedProfileDto.toDomain())
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error updating profile (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error updating profile: ${e.message}", e))
        }
    }
}