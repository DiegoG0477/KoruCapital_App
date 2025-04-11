package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName


data class CategoryListResponseDto(
    @SerializedName("data")
    val data: List<CategoryDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)