package com.koru.capital.home.domain.usecase

import com.koru.capital.home.domain.model.BusinessFeedItem
import com.koru.capital.home.domain.repository.HomeRepository
import com.koru.capital.home.domain.repository.PaginatedResult
import javax.inject.Inject

class GetBusinessesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    companion object {
        private const val DEFAULT_PAGE_LIMIT = 15
    }

    suspend operator fun invoke(
        page: Int,
        limit: Int = DEFAULT_PAGE_LIMIT,
        selectedCategoryId: String? = null,
        maxInvestment: Int? = null,
        isNearby: Boolean = false,
        otherFilters: Map<String, String> = emptyMap()
    ): Result<PaginatedResult<BusinessFeedItem>> {

        val apiParams = buildApiFilterParams(
            selectedCategoryId = selectedCategoryId,
            maxInvestment = maxInvestment,
            isNearby = isNearby,
            otherFilters = otherFilters
        )

        println("API Filter Params: $apiParams")
        return repository.getBusinesses(apiParams, page, limit)
    }

    private fun buildApiFilterParams(
        selectedCategoryId: String?,
        maxInvestment: Int?,
        isNearby: Boolean,
        otherFilters: Map<String, String>
    ): Map<String, String> {
        val params = mutableMapOf<String, String>()

        selectedCategoryId?.let { categoryId ->
            if (categoryId.isNotBlank()) {
                params["category_id"] = categoryId
            }
        }

        maxInvestment?.let { investment ->
            params["max_investment"] = investment.toString()
        }

        if (isNearby) {
            params["nearby"] = "true"
        }

        params.putAll(otherFilters)

        return params
    }
}