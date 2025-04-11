package com.koru.capital.home.domain.model

data class BusinessFeedItem(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val category: String?,
    val location: String?,
    val investmentRange: String?,
    val partnerCount: Int?,
    val description: String,
    val businessModel: String,
    val ownerName: String?,
    val ownerImageUrl: String?,
    val savedCount: Int?,
    val isSaved: Boolean,
    val isLiked: Boolean
)