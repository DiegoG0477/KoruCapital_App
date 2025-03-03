package com.koru.capital.business.data.mapper

import com.koru.capital.business.data.dto.AddBusinessRequestDto
import com.koru.capital.business.domain.Business

fun Business.toAddBusinessRequestDto(): AddBusinessRequestDto {
    return AddBusinessRequestDto(
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome
    )
}