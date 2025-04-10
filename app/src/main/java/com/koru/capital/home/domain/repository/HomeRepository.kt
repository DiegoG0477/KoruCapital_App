package com.koru.capital.home.domain.repository

import com.koru.capital.home.domain.model.BusinessFeedItem

// Result holder for paginated data in the domain layer
data class PaginatedResult<T>(
    val items: List<T>,
    val hasMore: Boolean,
    val nextPage: Int? // Or nextCursor: String?
)

// Defines operations related to the home feed
interface HomeRepository {
    /**
     * Fetches a paginated list of businesses for the feed.
     * @param filters A map of filter keys and values.
     * @param page The page number to retrieve.
     * @param limit The maximum number of items per page.
     * @return A Result containing a PaginatedResult of BusinessFeedItem on success, or an Exception on failure.
     */
    suspend fun getBusinesses(
        filters: Map<String, String>,
        page: Int,
        limit: Int
    ): Result<PaginatedResult<BusinessFeedItem>>

    /**
     * Toggles the 'saved' status of a business for the current user.
     * @param businessId The ID of the business to toggle.
     * @param currentState The current saved state (optional, depends on backend).
     * @return A Result containing Unit on success, or an Exception on failure.
     */
    suspend fun toggleSaveBusiness(businessId: String, currentState: Boolean): Result<Unit>

    /**
     * Toggles the 'liked' status of a business for the current user.
     * @param businessId The ID of the business to toggle.
     * @param currentState The current liked state (optional, depends on backend).
     * @return A Result containing Unit on success, or an Exception on failure.
     */
    suspend fun toggleLikeBusiness(businessId: String, currentState: Boolean): Result<Unit>
}