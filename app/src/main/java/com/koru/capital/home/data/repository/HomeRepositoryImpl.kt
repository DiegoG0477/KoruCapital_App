// capital/home/data/repository/HomeRepositoryImpl.kt
package com.koru.capital.home.data.repository

// Import generic wrapper
import com.koru.capital.core.data.dto.ApiResponseDto
// Specific DTO
import com.koru.capital.home.data.dto.PagedBusinessResponseDto
// Mapper and API Service
import com.koru.capital.home.data.mapper.toDomain
import com.koru.capital.home.data.datasource.HomeApiService
// Domain Model and Repository Interface
import com.koru.capital.home.domain.model.BusinessFeedItem
import com.koru.capital.home.domain.repository.HomeRepository
import com.koru.capital.home.domain.repository.PaginatedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService
) : HomeRepository {

    override suspend fun getBusinesses(
        filters: Map<String, String>,
        page: Int,
        limit: Int
    ): Result<PaginatedResult<BusinessFeedItem>> = withContext(Dispatchers.IO) {
        try {
            // Now returns Response<ApiResponseDto<PagedBusinessResponseDto>>
            val response = apiService.getBusinesses(filters, page, limit)

            // Check HTTP success AND status field AND data field
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val pagedData = response.body()!!.data!! // data is PagedBusinessResponseDto
                val domainList = pagedData.data.map { it.toDomain() } // Map items within paged data

                val paginationInfo = pagedData.pagination
                val hasMore = paginationInfo?.hasMore
                    ?: (paginationInfo?.nextPage != null)
                    ?: (domainList.size == limit) // Fallback logic

                val nextPageNum = paginationInfo?.nextPage ?: (page + 1)

                val paginatedResult = PaginatedResult(
                    items = domainList,
                    hasMore = hasMore,
                    nextPage = if (hasMore) nextPageNum else null
                )
                Result.success(paginatedResult)
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "API Error getting businesses (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error getting businesses: ${e.message}", e))
        }
    }

    // Helper for toggle operations
    private suspend fun handleToggleOperation(
        apiCall: suspend () -> Response<ApiResponseDto<Any?>>, // Lambda for the API call
        actionName: String // For error messages
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            // Check HTTP success AND status field
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(Unit)
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error toggling $actionName (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error toggling $actionName: ${e.message}", e))
        }
    }


    override suspend fun toggleSaveBusiness(businessId: String, currentState: Boolean): Result<Unit> {
        return handleToggleOperation(
            apiCall = { apiService.toggleSaveBusiness(businessId) },
            actionName = "save"
        )
    }

    override suspend fun toggleLikeBusiness(businessId: String, currentState: Boolean): Result<Unit> {
        return handleToggleOperation(
            apiCall = { apiService.toggleLikeBusiness(businessId) },
            actionName = "like"
        )
    }
}