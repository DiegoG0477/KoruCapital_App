package com.koru.capital.profile.data.dto

data class UpdateProfileRequestDto(
    val firstName: String,
    val lastName: String,
    val biography: String?,
    val linkedinProfile: String?,
    val instagramHandle: String?,
    val profileImageUrl: String?
)