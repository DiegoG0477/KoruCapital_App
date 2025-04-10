package com.koru.capital.auth.data.dto

// Data Transfer Object for the registration request body
data class RegisterRequestDto(
    val email: String,
    val password: String, // Send plain password, backend should hash it
    val firstName: String,
    val lastName: String,
    val birthDate: String, // Send as ISO String? e.g., "YYYY-MM-DD"
    val countryId: String,
    val stateId: String,
    val municipalityId: String
    // Include other fields matching RegistrationData
)