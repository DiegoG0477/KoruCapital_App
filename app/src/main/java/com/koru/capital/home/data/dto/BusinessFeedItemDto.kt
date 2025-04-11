package com.koru.capital.home.data.dto

data class BusinessFeedItemDto(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val categoryName: String?,
    val locationName: String?,
    val investmentMin: Double?,
    val investmentMax: Double?,
    val partnerCount: Int?,
    val description: String?,
    val businessModel: String?,
    val owner: OwnerDto?,
    val savedCount: Int?,
    val likedCount: Int?,
    val isSavedByUser: Boolean?,
    val isLikedByUser: Boolean?
)

data class OwnerDto(
    val userId: String?,
    val name: String?,
    val profileImageUrl: String?
)