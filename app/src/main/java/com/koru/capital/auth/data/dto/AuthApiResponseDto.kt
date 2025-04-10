package com.koru.capital.auth.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa la estructura completa de la respuesta de los endpoints
 * de login y register de la API, donde los datos del token están anidados.
 */
data class AuthApiResponseDto(
    @SerializedName("status")
    val status: String?, // ej: "success"

    @SerializedName("message")
    val message: String?,

    // La clave "data" contiene el objeto con los detalles del token
    @SerializedName("data")
    val data: LoginResponseDto? // Puede ser nulo si la operación falla pero devuelve 2xx
)