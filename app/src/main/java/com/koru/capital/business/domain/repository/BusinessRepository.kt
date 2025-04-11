package com.koru.capital.business.domain.repository

import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.presentation.viewmodel.BusinessFilter
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface BusinessRepository {

    suspend fun addBusiness(
        ownerId: RequestBody,
        name: RequestBody,
        description: RequestBody,
        investment: RequestBody,
        profitPercentage: RequestBody,
        categoryId: RequestBody,
        municipalityId: RequestBody,
        businessModel: RequestBody,
        monthlyIncome: RequestBody,
        imageUrl: MultipartBody.Part?
    ): Result<Business>

    suspend fun getBusinessDetails(businessId: String): Result<Business>

    suspend fun updateBusiness(
        businessId: String,
        name: RequestBody?,
        description: RequestBody?,
        investment: RequestBody?,
        profitPercentage: RequestBody?,
        categoryId: RequestBody?,
        municipalityId: RequestBody?,
        businessModel: RequestBody?,
        monthlyIncome: RequestBody?,
        imageUrl: MultipartBody.Part?
    ): Result<Business>

    suspend fun deleteBusiness(businessId: String): Result<Unit>

    suspend fun getMyBusinesses(filter: BusinessFilter): Result<List<com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel>>
}