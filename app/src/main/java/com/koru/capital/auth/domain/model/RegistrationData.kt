package com.koru.capital.auth.domain.model

import java.time.LocalDate

data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val countryId: String,
    val stateId: String,
    val municipalityId: String
)
