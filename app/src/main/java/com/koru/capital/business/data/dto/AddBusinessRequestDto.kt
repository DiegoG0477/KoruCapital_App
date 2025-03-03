package com.koru.capital.business.data.dto

data class AddBusinessRequestDto(
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val categoryId: Number,
    val municipalityId: Number,
    val businessModel: String,
    val monthlyIncome: Double
)