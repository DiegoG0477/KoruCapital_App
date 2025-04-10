package com.koru.capital.profile.data.dto

import com.google.gson.annotations.SerializedName // Importar anotación

/**
 * Data Transfer Object para recibir datos del perfil desde la API.
 * Usa @SerializedName para mapear los nombres del JSON de la API
 * (ej. 'first_name') a los nombres de campo de Kotlin (ej. 'firstName').
 */
data class UserProfileDto(
    // Asumiendo que la API devuelve 'id' dentro del objeto de usuario
    @SerializedName("id") // Si la API devuelve 'userId', no necesitas esto
    val userId: String,

    @SerializedName("first_name") // Mapear 'first_name' de la API
    val firstName: String,

    @SerializedName("last_name") // Mapear 'last_name' de la API
    val lastName: String,

    @SerializedName("email") // Mapear 'email' de la API (coincide pero es buena práctica)
    val email: String,

    @SerializedName("profile_image_url") // Mapear 'profile_image_url' de la API
    val profileImageUrl: String?,

    @SerializedName("biography") // Mapear 'biography' de la API
    val biography: String?,

    @SerializedName("linkedin_profile") // Mapear 'linkedin_profile' de la API
    val linkedinProfile: String?, // URL completa desde la API

    @SerializedName("instagram_handle") // Mapear 'instagram_handle' de la API
    val instagramHandle: String?, // Solo el handle desde la API

    // Mapear 'created_at' de la API (formato ISO String) a 'memberSince'
    @SerializedName("created_at")
    val memberSince: String? // El ProfileMapper lo formateará
)