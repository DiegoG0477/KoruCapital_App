package com.koru.capital.profile.data.dto

// DTO for the PUT /profile/me request body
data class UpdateProfileRequestDto(
    val firstName: String,
    val lastName: String,
    val biography: String?, // Match API field names
    val linkedinProfile: String?,
    val instagramHandle: String?, // Or full URL depending on API
    val profileImageUrl: String? // URL of the (potentially new) profile image
)