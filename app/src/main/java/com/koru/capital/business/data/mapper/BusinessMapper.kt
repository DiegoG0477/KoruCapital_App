package com.koru.capital.business.data.mapper

import com.koru.capital.profile.data.datasource.AddBusinessRequestDto
import com.koru.capital.business.data.dto.BusinessDto
import com.koru.capital.business.data.dto.BusinessListItemDto
import com.koru.capital.business.data.dto.UpdateBusinessRequestDto
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel

fun BusinessDto.toDomain(): Business {
    val location = formatLocation(municipalityName, stateName)

    return Business(
        id = this.id,
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId.toString(),
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,

        categoryName = this.categoryName,
        locationName = location,
        imageUrl = this.imageUrls?.firstOrNull(),
        ownerUserId = this.ownerInfo?.userId,
        ownerName = this.ownerInfo?.name,
        ownerEmail = this.ownerInfo?.email,
        ownerPhone = this.ownerInfo?.phone,
        ownerLinkedIn = this.ownerInfo?.linkedInUrl,
        ownerImageUrl = this.ownerInfo?.profileImageUrl,

        isSavedByUser = false,
        isLikedByUser = false,
    )
}

private fun formatLocation(municipality: String?, state: String?): String? {
    return when {
        !municipality.isNullOrBlank() && !state.isNullOrBlank() -> "$municipality, $state"
        !municipality.isNullOrBlank() -> municipality
        !state.isNullOrBlank() -> state
        else -> null
    }
}

fun Business.toAddBusinessRequestDto(): AddBusinessRequestDto {
    return AddBusinessRequestDto(
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,
        imageUrl = this.imageUrl
    )
}

fun Business.toUpdateBusinessRequestDto(): UpdateBusinessRequestDto {
    return UpdateBusinessRequestDto(
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,
        imageUrl = this.imageUrl
    )
}

fun BusinessListItemDto.toUiModel(): BusinessListItemUiModel {
    return BusinessListItemUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        category = this.category,
        location = this.location,
        isOwned = this.isOwned
    )
}
