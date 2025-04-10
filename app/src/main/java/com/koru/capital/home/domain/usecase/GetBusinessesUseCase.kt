package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.model.BusinessFeedItem
import com.koru.capital.home.domain.repository.HomeRepository
import com.koru.capital.home.domain.repository.PaginatedResult
// Import FilterType if needed for logic, but primarily rely on passed values
// import com.koru.capital.home.presentation.ui.components.FilterType
import javax.inject.Inject

class GetBusinessesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    companion object {
        private const val DEFAULT_PAGE_LIMIT = 15
    }

    /**
     * Fetches businesses based on specific filter values and pagination.
     * @param page The page number to retrieve.
     * @param limit The maximum number of items per page.
     * @param selectedCategoryId The ID of the category to filter by, if any.
     * @param maxInvestment The maximum investment amount to filter by, if any.
     * @param isNearby Filter by nearby businesses, if true.
     * @param otherFilters A map for any other custom filters needed by the API.
     * @return Result containing paginated business feed items.
     */
    suspend operator fun invoke(
        page: Int,
        limit: Int = DEFAULT_PAGE_LIMIT,
        // Specific, known filter parameters
        selectedCategoryId: String? = null,
        maxInvestment: Int? = null,
        isNearby: Boolean = false,
        // Optional: Generic map for less common or dynamic filters
        otherFilters: Map<String, String> = emptyMap()
    ): Result<PaginatedResult<BusinessFeedItem>> {

        // Build the API filter map directly from the provided values
        val apiParams = buildApiFilterParams(
            selectedCategoryId = selectedCategoryId,
            maxInvestment = maxInvestment,
            isNearby = isNearby,
            otherFilters = otherFilters
        )

        println("API Filter Params: $apiParams") // For debugging
        return repository.getBusinesses(apiParams, page, limit)
    }

    /**
     * Builds the map of query parameters for the API based on filter values.
     * ***ADAPT PARAMETER NAMES AND VALUES TO YOUR ACTUAL API***
     */
    private fun buildApiFilterParams(
        selectedCategoryId: String?,
        maxInvestment: Int?,
        isNearby: Boolean,
        otherFilters: Map<String, String>
    ): Map<String, String> {
        val params = mutableMapOf<String, String>()

        // Add category filter if ID is present
        selectedCategoryId?.let { categoryId ->
            if (categoryId.isNotBlank()) {
                params["category_id"] = categoryId // ADJUST PARAM NAME IF NEEDED
            }
        }

        // Add max investment filter if value is present
        maxInvestment?.let { investment ->
            // Example: API expects 'max_investment=50000'
            params["max_investment"] = investment.toString() // ADJUST PARAM NAME IF NEEDED
        }

        // Add nearby filter if requested
        if (isNearby) {
            // Example: API expects 'nearby=true'
            params["nearby"] = "true" // ADJUST PARAM NAME IF NEEDED
        }

        // Add any other generic filters
        params.putAll(otherFilters)

        return params
    }
}