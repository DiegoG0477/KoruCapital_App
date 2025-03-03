package com.koru.capital.business.domain

data class Business(
    val name: String,
    val description: String,
    val investment: Double,
    val profitPercentage: Double,
    val category: String,
    val businessModel: String,
    val monthlyIncome: Double
)