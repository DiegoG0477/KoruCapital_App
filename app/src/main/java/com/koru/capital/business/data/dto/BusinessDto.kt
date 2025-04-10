package com.koru.capital.business.data.dto

// Example DTO - Adapt based on actual API response for business details
data class BusinessDto(
    val id: String, // Assuming API returns ID in details
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int, // Assuming IDs are returned
    val categoryName: String?, // Optional name if API provides it
    val municipalityId: Int,
    val municipalityName: String?,
    val stateName: String?, // May need state info too
    val businessModel: String,
    val monthlyIncome: Double,
    val imageUrls: List<String>?, // Allow multiple images?
    val ownerInfo: OwnerContactDto? // Nested owner contact details
)

data class OwnerContactDto(
    val userId: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val linkedInUrl: String?,
    val profileImageUrl: String?
)