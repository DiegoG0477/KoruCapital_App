package com.koru.capital.home.data.mapper

import com.koru.capital.home.data.dto.BusinessFeedItemDto
import com.koru.capital.home.domain.model.BusinessFeedItem
import com.koru.capital.home.presentation.viewmodel.BusinessCardUiModel
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

// --- DTO to Domain ---
fun BusinessFeedItemDto.toDomain(): BusinessFeedItem {
    // Use helper for investment range formatting
    val investmentRangeFormatted = formatInvestmentRange(investmentMin, investmentMax)

    return BusinessFeedItem(
        id = this.id,
        imageUrl = this.imageUrl,
        title = this.title ?: "Oportunidad sin título", // Provide default
        category = this.categoryName,
        location = this.locationName,
        investmentRange = investmentRangeFormatted, // Use formatted string
        partnerCount = this.partnerCount,
        description = this.description ?: "", // Provide default
        businessModel = this.businessModel ?: "", // Provide default
        ownerName = this.owner?.name,
        ownerImageUrl = this.owner?.profileImageUrl,
        savedCount = this.savedCount,
        // Crucially rely on the API flag if provided, otherwise default to false
        isSaved = this.isSavedByUser ?: false,
        isLiked = this.isLikedByUser ?: false
    )
}

/**
 * Helper to format investment min/max from DTO into a display string for the domain.
 * Example formats: "<50k", "50k-100k", ">100k", "N/A"
 */
private fun formatInvestmentRange(min: Double?, max: Double?): String? {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "MX")).apply {
        maximumFractionDigits = 0 // No decimals for 'k' representation
    }

    val minK = min?.takeIf { it > 0 }?.div(1000)?.roundToInt()
    val maxK = max?.takeIf { it > 0 }?.div(1000)?.roundToInt()

    return when {
        minK != null && maxK != null -> "${formatter.format(minK)}k - ${formatter.format(maxK)}k"
        maxK != null -> "< ${formatter.format(maxK)}k"
        minK != null -> "> ${formatter.format(minK)}k"
        else -> null // Or return "N/A" or some default?
    }
}


// --- Domain to Presentation (UI Model) ---
// This mapping is often simple, transferring data as is, but can add UI-specific formatting if needed.
fun BusinessFeedItem.toUiModel(): BusinessCardUiModel {
    return BusinessCardUiModel(
        id = this.id,
        imageUrl = this.imageUrl,
        title = this.title,
        category = this.category,
        location = this.location,
        investmentRange = this.investmentRange,
        partnerCount = this.partnerCount,
        description = this.description,
        businessModel = this.businessModel,
        ownerName = this.ownerName,
        ownerImageUrl = this.ownerImageUrl,
        savedCount = this.savedCount,
        isSaved = this.isSaved, // Pass state through
        isLiked = this.isLiked  // Pass state through
    )
}