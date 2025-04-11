package com.koru.capital.auth.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null
)
