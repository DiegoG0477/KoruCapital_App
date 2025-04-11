package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName


data class CountryListResponseDto(
    @SerializedName("data")
    val data: List<CountryDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)