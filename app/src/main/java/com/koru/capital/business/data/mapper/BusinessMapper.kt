package com.koru.capital.business.data.mapper

import com.koru.capital.profile.data.datasource.AddBusinessRequestDto
import com.koru.capital.business.data.dto.BusinessDto // Import detail DTO
import com.koru.capital.business.data.dto.BusinessListItemDto
import com.koru.capital.business.data.dto.UpdateBusinessRequestDto
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel

// --- DTO to Domain (For Business Details) ---
fun BusinessDto.toDomain(): Business {
    // Helper to create a combined location string
    val location = formatLocation(municipalityName, stateName)
    // Example formatting for investment range (can be done here or in domain/viewmodel)
    // val investmentRangeStr = formatInvestmentRange(investmentMin, investmentMax) // Assuming DTO had min/max

    return Business(
        id = this.id, // Assuming API returns the ID in the details response
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,

        // Populate display-oriented fields from DTO
        categoryName = this.categoryName,
        locationName = location,
        // investmentRange = investmentRangeStr, // Assign formatted range
        imageUrl = this.imageUrls?.firstOrNull(), // Take first image or handle list
        ownerUserId = this.ownerInfo?.userId,
        ownerName = this.ownerInfo?.name,
        ownerEmail = this.ownerInfo?.email,
        ownerPhone = this.ownerInfo?.phone,
        ownerLinkedIn = this.ownerInfo?.linkedInUrl,
        ownerImageUrl = this.ownerInfo?.profileImageUrl,

        // Assume details API doesn't directly return user-specific saved/liked state
        // These might need to be fetched/merged separately or handled in ViewModel
        isSavedByUser = false, // Default unless API provides it
        isLikedByUser = false, // Default unless API provides it
        // savedCount = this.savedCount, // If API provides counts
        // likeCount = this.likeCount
    )
}

// Helper function to combine location parts (Example)
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
        imageUrl = this.imageUrl // Include image URL
    )
}

// --- Domain to DTO (Update Request) ---
fun Business.toUpdateBusinessRequestDto(): UpdateBusinessRequestDto {
    // Map relevant fields for update
    return UpdateBusinessRequestDto(
        name = this.name,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        categoryId = this.categoryId,
        municipalityId = this.municipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,
        imageUrl = this.imageUrl // Include potentially updated image URL
    )
}

fun BusinessListItemDto.toUiModel(): BusinessListItemUiModel {
    return BusinessListItemUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        category = this.category, // Los nombres ya coinciden con el UI Model
        location = this.location, // Los nombres ya coinciden con el UI Model
        isOwned = this.isOwned   // Los nombres ya coinciden con el UI Model
    )
}

// Example helper (if needed here, otherwise can be elsewhere)
// private fun formatInvestmentRange(min: Double?, max: Double?): String? { ... }