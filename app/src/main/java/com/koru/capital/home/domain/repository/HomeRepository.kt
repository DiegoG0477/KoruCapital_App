package com.koru.capital.home.domain.repository

import com.koru.capital.home.domain.model.BusinessFeedItem

data class PaginatedResult<T>(
    val items: List<T>,
    val hasMore: Boolean,
    val nextPage: Int?
)

interface HomeRepository {
    suspend fun getBusinesses(
        filters: Map<String, String>,
        page: Int,
        limit: Int
    ): Result<PaginatedResult<BusinessFeedItem>>

    suspend fun toggleSaveBusiness(businessId: String, currentState: Boolean): Result<Unit>

    suspend fun toggleLikeBusiness(businessId: String, currentState: Boolean): Result<Unit>
}