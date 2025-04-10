package com.koru.capital.business.domain.model

// Represents the detailed Business information in the Domain Layer
data class Business(
    val id: String? = null, // Make ID optional or mandatory based on usage (mandatory for details/update)
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int, // Keep IDs for internal logic/requests
    val municipalityId: Int,
    val businessModel: String,
    val monthlyIncome: Double,

    // Fields often needed for Display (populated by GetBusinessDetailsUseCase)
    val categoryName: String? = null,
    val locationName: String? = null, // e.g., "Zapopan, JAL"
    val investmentRange: String? = null, // Formatted string, e.g., "100k-150k" (can be derived)
    val imageUrl: String? = null, // Or imageUrls: List<String>? = null for multiple images
    val ownerUserId: String? = null,
    val ownerName: String? = null,
    val ownerEmail: String? = null,
    val ownerPhone: String? = null,
    val ownerLinkedIn: String? = null,
    val ownerImageUrl: String? = null,

    // State flags (potentially populated based on user context)
    val isOwnedByUser: Boolean = false, // Can be inferred if ownerUserId matches current user
    val isSavedByUser: Boolean = false,
    val isLikedByUser: Boolean = false,
    val savedCount: Int? = null,
    val likeCount: Int? = null // If tracking like counts too
)