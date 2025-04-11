package com.koru.capital.core.data.dto

import com.google.gson.annotations.SerializedName


data class CountryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)