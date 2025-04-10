package com.koru.capital.home.data.dto

// Data Transfer Object for the paginated API response containing feed items
data class PagedBusinessResponseDto(
    // Field name "data" or "items" or "results" - match your API
    val data: List<BusinessFeedItemDto>,
    val pagination: PaginationInfoDto? // Optional pagination metadata
)

// Data Transfer Object for pagination metadata from the API
data class PaginationInfoDto(
    val currentPage: Int?,
    val totalPages: Int?,
    val totalItems: Int?,
    val limit: Int?,
    val nextPage: Int?, // Page number for the next request
    val hasMore: Boolean? // Alternative/addition to nextPage
    // val nextCursor: String? // Use if API uses cursor pagination
)