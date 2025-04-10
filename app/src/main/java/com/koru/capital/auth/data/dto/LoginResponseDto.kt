package com.koru.capital.auth.data.dto

// Data Transfer Object for the login API response details (nested within "data")
data class LoginResponseDto(
    // Field names must match the JSON response from your API within the "data" object
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?, // e.g., "Bearer"
    val expiresIn: Long? // e.g., 3600 (seconds)
)