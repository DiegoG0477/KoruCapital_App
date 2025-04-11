package com.koru.capital.auth.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.koru.capital.auth.data.dto.LoginRequestDto
import com.koru.capital.auth.data.dto.LoginResponseDto
import com.koru.capital.auth.data.dto.RegisterRequestDto
import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.model.RegistrationData
import java.time.format.DateTimeFormatter

fun LoginCredentials.toRequestDto(): LoginRequestDto {
    return LoginRequestDto(
        email = this.email,
        password = this.password
    )
}

fun LoginResponseDto.toDomain(): AuthToken? {
    return this.accessToken?.let { token ->
        AuthToken(
            accessToken = token,
            refreshToken = this.refreshToken,
            expiresIn = this.expiresIn
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun RegistrationData.toRequestDto(): RegisterRequestDto {
    val birthDateString = this.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

    return RegisterRequestDto(
        email = this.email,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        birthDate = birthDateString,
        countryId = this.countryId,
        stateId = this.stateId,
        municipalityId = this.municipalityId
    )
}