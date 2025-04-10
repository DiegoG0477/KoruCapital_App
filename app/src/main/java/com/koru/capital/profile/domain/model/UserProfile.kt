package com.koru.capital.profile.domain.model

data class UserProfile(
    val userId: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?, // Assuming email is part of the profile
    val profileImageUrl: String?,
    val bio: String?,
    val linkedInUrl: String?,
    val instagramUrl: String?,
    // Add other fields like location, interests, etc. if needed
    // val city: String?,
    // val state: String?,
    val joinDate: String? // Or LocalDate/Date
)