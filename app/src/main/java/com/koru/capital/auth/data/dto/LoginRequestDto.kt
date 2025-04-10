package com.koru.capital.auth.data.dto

// Data Transfer Object for the login request body
data class LoginRequestDto(
    val email: String,
    val password: String
)