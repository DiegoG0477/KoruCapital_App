package com.koru.capital.profile.domain.model

data class UserProfileUpdate(
    val firstName: String,
    val lastName: String,
    val bio: String?,
    val linkedInUrl: String?,
    val instagramUrl: String?,
    // Profile image URL update might be handled separately via UploadFileUseCase result
    // Or included here if the backend accepts the URL directly in the profile update
    val profileImageUrl: String? = null // Include if updating URL via this call
    // Add other updatable fields (location?) but likely not email/password here
)