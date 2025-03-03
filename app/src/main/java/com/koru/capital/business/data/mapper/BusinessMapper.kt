package com.koru.capital.business.data.mapper

import com.koru.capital.business.data.remote.dto.AddBusinessRequestDto
import com.koru.capital.business.domain.Business

fun Business.toAddBusinessRequestDto(): AddBusinessRequestDto {
    return AddBusinessRequestDto(
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        category = this.category,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome
    )
}