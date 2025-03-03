package com.koru.capital.business.data.remote.dto

data class AddBusinessRequestDto(
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val category: String,
    val businessModel: String,
    val monthlyIncome: Double
)