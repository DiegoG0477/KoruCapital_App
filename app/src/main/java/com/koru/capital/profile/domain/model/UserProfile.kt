package com.koru.capital.profile.domain.model

data class UserProfile(
    val userId: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val profileImageUrl: String?,
    val bio: String?,
    val linkedInUrl: String?,
    val instagramUrl: String?,
    val joinDate: String?
)