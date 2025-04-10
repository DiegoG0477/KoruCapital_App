package com.koru.capital.profile.data.datasource

/**
 * DTO para enviar la solicitud de creación de negocio.
 * Tipos ajustados para coincidir con lo que la API espera (números).
 */
data class AddBusinessRequestDto(
    val name: String,
    val description: String,
    val investment: Double, // Mantenido como Double
    val profitPercentage: Double, // Mantenido como Double
    val categoryId: Int, // Cambiado a Int
    val municipalityId: Int, // Cambiado a Int (API convertirá si es necesario)
    val businessModel: String,
    val monthlyIncome: Double, // Mantenido como Double
    val imageUrl: String? // URL es opcional en el DTO base, la imagen va por Multipart
)