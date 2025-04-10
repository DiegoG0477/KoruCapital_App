package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de la lista de municipios (envuelta en "data").
 */
data class MunicipalityListResponseDto(
    @SerializedName("data") // Verifica que la clave sea "data" en tu API
    val data: List<MunicipalityDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)