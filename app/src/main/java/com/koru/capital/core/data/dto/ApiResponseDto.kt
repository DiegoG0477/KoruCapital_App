// capital/core/data/dto/ApiResponseDto.kt
package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Generic DTO representing the common API response structure.
 * @param T The type of the data expected within the "data" field.
 */
data class ApiResponseDto<T>(
    @SerializedName("status")
    val status: String?, // e.g., "success", "error"

    @SerializedName("message")
    val message: String?, // Message from the API

    @SerializedName("data")
    val data: T? // The actual payload (can be an object, a list, or null)
)