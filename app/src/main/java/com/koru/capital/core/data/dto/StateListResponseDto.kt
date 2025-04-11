package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName


data class StateListResponseDto(
    @SerializedName("data")
    val data: List<StateDto>,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)