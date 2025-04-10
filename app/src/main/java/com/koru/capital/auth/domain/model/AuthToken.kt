package com.koru.capital.auth.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null, // Optional refresh token
    val expiresIn: Long? = null // Optional expiry duration in seconds
)
