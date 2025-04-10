package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para representar la respuesta completa de la API para la lista de países,
 * asumiendo que la lista viene dentro de una clave (ej: "data").
 * ¡Ajusta el @SerializedName("data") si la clave en tu API es diferente!
 */
data class CountryListResponseDto(
    // Usa @SerializedName si el nombre de la clave en el JSON no coincide exactamente con "data"
    @SerializedName("data") // <-- CAMBIO: ¡¡Verifica que la clave en tu API sea "data"!! (Podría ser "countries", "results", etc.)
    val data: List<CountryDto>,

    // Opcional: Incluye otros campos si la API los devuelve en el objeto principal
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
    // Añade campos de paginación si existen
)