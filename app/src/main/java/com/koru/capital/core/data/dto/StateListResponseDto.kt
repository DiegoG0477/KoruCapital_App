package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de la lista de estados (envuelta en "data").
 */
data class StateListResponseDto(
    @SerializedName("data") // Verifica que la clave sea "data" en tu API
    val data: List<StateDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)