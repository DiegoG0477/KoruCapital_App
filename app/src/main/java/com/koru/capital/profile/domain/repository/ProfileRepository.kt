package com.koru.capital.profile.domain.repository

import com.koru.capital.profile.domain.model.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileRepository {
    suspend fun getMyProfile(): Result<UserProfile>

    suspend fun updateMyProfile(
        firstName: RequestBody,
        lastName: RequestBody,
        biography: RequestBody?,
        linkedinProfile: RequestBody?,
        instagramHandle: RequestBody?,
        profileImage: MultipartBody.Part?
    ): Result<UserProfile>
}