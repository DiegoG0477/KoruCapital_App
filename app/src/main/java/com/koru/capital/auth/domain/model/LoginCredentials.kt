package com.koru.capital.auth.domain.model

// Represents the data entered by the user for login
data class LoginCredentials(
    val email: String,
    val password: String
)