package com.koru.capital.business.data.repository

import com.koru.capital.core.data.dto.ApiResponseDto
import com.koru.capital.business.data.datasource.BusinessApiService
import com.koru.capital.business.data.dto.BusinessDto
import com.koru.capital.business.data.dto.BusinessListItemDto
import com.koru.capital.business.data.mapper.toDomain
import com.koru.capital.business.data.mapper.toUiModel
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.business.presentation.viewmodel.BusinessFilter
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
) : BusinessRepository {

    private suspend fun handleBusinessDtoResponse(
        apiCall: suspend () -> Response<ApiResponseDto<BusinessDto>>,
        actionName: String
    ): Result<Business> = withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val businessDto = response.body()!!.data!!
                Result.success(businessDto.toDomain())
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error $actionName business (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error $actionName business: ${e.message}", e))
        }
    }


    override suspend fun addBusiness(
        ownerId: RequestBody, name: RequestBody, description: RequestBody,
        investment: RequestBody, profitPercentage: RequestBody, categoryId: RequestBody,
        municipalityId: RequestBody, businessModel: RequestBody, monthlyIncome: RequestBody,
        imageUrl: MultipartBody.Part?
    ): Result<Business> {
        return handleBusinessDtoResponse(
            actionName = "adding",
            apiCall = {
                apiService.addBusiness(
                    ownerId, name, description, investment, profitPercentage, categoryId,
                    municipalityId, businessModel, monthlyIncome, imageUrl
                )
            }
        )
    }

    override suspend fun getBusinessDetails(businessId: String): Result<Business> {
        return handleBusinessDtoResponse(
            actionName = "fetching details for",
            apiCall = { apiService.getBusinessDetails(businessId) }
        )
    }

    override suspend fun updateBusiness(
        businessId: String, name: RequestBody?, description: RequestBody?,
        investment: RequestBody?, profitPercentage: RequestBody?, categoryId: RequestBody?,
        municipalityId: RequestBody?, businessModel: RequestBody?, monthlyIncome: RequestBody?,
        imageUrl: MultipartBody.Part?
    ): Result<Business> {
        return handleBusinessDtoResponse(
            actionName = "updating",
            apiCall = {
                apiService.updateBusiness(
                    businessId, name, description, investment, profitPercentage, categoryId,
                    municipalityId, businessModel, monthlyIncome, imageUrl
                )
            }
        )
    }

    override suspend fun deleteBusiness(businessId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteBusiness(businessId)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(Unit)
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error deleting business (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error deleting business: ${e.message}", e))
        }
    }

    override suspend fun getMyBusinesses(filter: BusinessFilter): Result<List<BusinessListItemUiModel>> = withContext(Dispatchers.IO) {
        try {
            val filterString = filter.name
            val response = apiService.getMyBusinesses(filter = filterString)

            if (response.isSuccessful && response.body()?.status == "success" && response.body()?.data != null) {
                val dtoList = response.body()!!.data!!
                val uiModelList = dtoList.map { dto -> dto.toUiModel() }
                Result.success(uiModelList)
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()?.take(200)
                    ?: "Error fetching my businesses (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error fetching my businesses: ${e.message}", e))
        }
    }
}