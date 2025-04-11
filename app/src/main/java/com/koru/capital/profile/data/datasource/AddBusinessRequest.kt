package com.koru.capital.profile.data.datasource


data class AddBusinessRequestDto(
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