package com.koru.capital.business.domain.model

data class Business(
    val id: String? = null,
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int,
    val municipalityId: String,
    val businessModel: String,
    val monthlyIncome: Double,

    val categoryName: String? = null,
    val locationName: String? = null,
    val investmentRange: String? = null,
    val imageUrl: String? = null,
    val ownerUserId: String? = null,
    val ownerName: String? = null,
    val ownerEmail: String? = null,
    val ownerPhone: String? = null,
    val ownerLinkedIn: String? = null,
    val ownerImageUrl: String? = null,

    val isOwnedByUser: Boolean = false,
    val isSavedByUser: Boolean = false,
    val isLikedByUser: Boolean = false,
    val savedCount: Int? = null,
    val likeCount: Int? = null
)