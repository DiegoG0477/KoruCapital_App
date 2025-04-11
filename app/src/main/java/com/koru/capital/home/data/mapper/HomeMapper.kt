package com.koru.capital.home.data.mapper

import com.koru.capital.home.data.dto.BusinessFeedItemDto
import com.koru.capital.home.domain.model.BusinessFeedItem
import com.koru.capital.home.presentation.viewmodel.BusinessCardUiModel
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

fun BusinessFeedItemDto.toDomain(): BusinessFeedItem {
    val investmentRangeFormatted = formatInvestmentRange(investmentMin, investmentMax)

    return BusinessFeedItem(
        id = this.id,
        imageUrl = this.imageUrl,
        title = this.title ?: "Oportunidad sin título",
        category = this.categoryName,
        location = this.locationName,
        investmentRange = investmentRangeFormatted,
        partnerCount = this.partnerCount,
        description = this.description ?: "",
        businessModel = this.businessModel ?: "",
        ownerName = this.owner?.name,
        ownerImageUrl = this.owner?.profileImageUrl,
        savedCount = this.savedCount,
        isSaved = this.isSavedByUser ?: false,
        isLiked = this.isLikedByUser ?: false
    )
}


private fun formatInvestmentRange(min: Double?, max: Double?): String? {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "MX")).apply {
        maximumFractionDigits = 0
    }

    val minK = min?.takeIf { it > 0 }?.div(1000)?.roundToInt()
    val maxK = max?.takeIf { it > 0 }?.div(1000)?.roundToInt()

    return when {
        minK != null && maxK != null -> "${formatter.format(minK)}k - ${formatter.format(maxK)}k"
        maxK != null -> "< ${formatter.format(maxK)}k"
        minK != null -> "> ${formatter.format(minK)}k"
        else -> null
    }
}


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
        isSaved = this.isSaved,
        isLiked = this.isLiked
    )
}