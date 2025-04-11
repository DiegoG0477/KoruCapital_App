package com.koru.capital.home.data.dto

import com.google.gson.annotations.SerializedName


data class BusinessFeedApiResponseDto(
    @SerializedName("status")
    val status: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: List<BusinessFeedItemDto>?,

    @SerializedName("pagination")
    val pagination: PaginationInfoDto?
)

data class PaginationInfoDto(
    val currentPage: Int?,
    val totalPages: Int?,
    val totalItems: Int?,
    val limit: Int?,
    val nextPage: Int?,
    val hasMore: Boolean?
)