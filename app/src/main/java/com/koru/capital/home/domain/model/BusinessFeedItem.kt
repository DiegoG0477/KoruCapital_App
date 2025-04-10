package com.koru.capital.home.domain.model

// Represents a single business item as needed for the home feed in the Domain Layer
data class BusinessFeedItem(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val category: String?, // Display name of the category
    val location: String?, // Formatted location string (e.g., "City, State")
    val investmentRange: String?, // Formatted range (e.g., "50k-100k")
    val partnerCount: Int?,
    val description: String, // Short description for the card
    val businessModel: String, // Short model description for the card
    val ownerName: String?,
    val ownerImageUrl: String?,
    val savedCount: Int?,
    // State flags relevant to the current user viewing the feed
    val isSaved: Boolean,
    val isLiked: Boolean
    // Add other domain-specific fields if necessary
)