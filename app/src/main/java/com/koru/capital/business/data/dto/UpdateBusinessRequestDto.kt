package com.koru.capital.business.data.dto
// Similar to Add, but might not need all fields if API allows partial updates
data class UpdateBusinessRequestDto(
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int,
    val municipalityId: Int,
    val businessModel: String,
    val monthlyIncome: Double,
    val imageUrl: String?
)