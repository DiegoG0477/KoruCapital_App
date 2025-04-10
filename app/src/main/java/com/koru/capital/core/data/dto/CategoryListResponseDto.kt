package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de la lista de categorías (envuelta en "data").
 */
data class CategoryListResponseDto(
    @SerializedName("data") // Verifica que la clave sea "data" en tu API
    val data: List<CategoryDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)