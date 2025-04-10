package com.koru.capital.auth.domain.model

import java.time.LocalDate // Use java.time

// Represents all data collected during registration process
data class RegistrationData(
    // Step 1
    val email: String,
    val password: String,
    // Step 2
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val countryId: String, // Assuming we send IDs
    val stateId: String,
    val municipalityId: String
    // Add other fields if collected (e.g., phone number)
)
