package com.koru.capital.business.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para representar un elemento simplificado de negocio
 * en listas, como la de "Mis Negocios".
 * Coincide con BusinessListItemUiModel de la app.
 */
data class BusinessListItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl") // API puede devolver null
    val imageUrl: String?,

    // La API devuelve categoryName y locationName (municipio, estado)
    @SerializedName("category") // Campo esperado por UIModel
    val category: String?,

    @SerializedName("location") // Campo esperado por UIModel
    val location: String?,

    @SerializedName("isOwned") // Indica si el negocio pertenece al usuario que consulta
    val isOwned: Boolean
)