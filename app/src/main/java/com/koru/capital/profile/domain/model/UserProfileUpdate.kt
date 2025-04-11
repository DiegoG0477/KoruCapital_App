package com.koru.capital.profile.domain.model

data class UserProfileUpdate(
    val firstName: String,
    val lastName: String,
    val bio: String?,
    val linkedInUrl: String?,
    val instagramUrl: String?,
    val profileImageUrl: String? = null
)