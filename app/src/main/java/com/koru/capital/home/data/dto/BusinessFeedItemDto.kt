package com.koru.capital.home.data.dto

// Data Transfer Object for a single business item in the feed list from the API
data class BusinessFeedItemDto(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val categoryName: String?, // Name might be more useful than ID for direct display
    val locationName: String?, // e.g., "Guadalajara, Jalisco"
    val investmentMin: Double?, // API might return range components
    val investmentMax: Double?,
    val partnerCount: Int?,
    val description: String?, // Short description for feed card
    val businessModel: String?, // Short model for feed card
    val owner: OwnerDto?, // Nested owner summary
    val savedCount: Int?,
    val likedCount: Int?, // If API provides like counts
    val isSavedByUser: Boolean?, // State specific to the requesting user
    val isLikedByUser: Boolean?  // State specific to the requesting user
)

// Summary DTO for the owner shown in the feed card
data class OwnerDto(
    val userId: String?, // Include ID if needed for navigation to their profile
    val name: String?,
    val profileImageUrl: String?
)