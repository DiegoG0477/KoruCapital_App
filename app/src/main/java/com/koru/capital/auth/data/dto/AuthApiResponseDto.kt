package com.koru.capital.auth.data.dto

import com.google.gson.annotations.SerializedName


data class AuthApiResponseDto(
    @SerializedName("status")
    val status: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: LoginResponseDto?
)