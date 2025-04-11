package com.koru.capital.business.data.dto

import com.google.gson.annotations.SerializedName


data class BusinessListItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("isOwned")
    val isOwned: Boolean
)