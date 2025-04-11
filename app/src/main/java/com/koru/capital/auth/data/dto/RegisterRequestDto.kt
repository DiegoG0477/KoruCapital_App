package com.koru.capital.auth.data.dto

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val countryId: String,
    val stateId: String,
    val municipalityId: String
)