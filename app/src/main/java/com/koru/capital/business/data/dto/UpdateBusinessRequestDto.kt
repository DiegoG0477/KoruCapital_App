package com.koru.capital.business.data.dto
data class UpdateBusinessRequestDto(
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Int,
    val municipalityId: String,
    val businessModel: String,
    val monthlyIncome: Double,
    val imageUrl: String?
)