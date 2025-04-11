package com.koru.capital.auth.data.dto

data class LoginResponseDto(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?,
    val expiresIn: Long?
)