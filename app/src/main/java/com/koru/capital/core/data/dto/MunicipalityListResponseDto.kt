package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName


data class MunicipalityListResponseDto(
    @SerializedName("data")
    val data: List<MunicipalityDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)