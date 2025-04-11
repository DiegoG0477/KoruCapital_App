package com.koru.capital.business.data.dto

data class BusinessDto(
    val id: String,
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int,
    val categoryName: String?,
    val municipalityId: String,
    val municipalityName: String?,
    val stateName: String?,
    val businessModel: String,
    val monthlyIncome: Double,
    val imageUrls: List<String>?,
    val ownerInfo: OwnerContactDto?
)

data class OwnerContactDto(
    val userId: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val linkedInUrl: String?,
    val profileImageUrl: String?
)