package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para representar un país recibido de la API.
 */
data class CountryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)