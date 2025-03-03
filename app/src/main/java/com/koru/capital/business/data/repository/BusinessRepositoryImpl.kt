package com.koru.capital.business.data.repository

import com.koru.capital.business.data.mapper.toAddBusinessRequestDto
import com.koru.capital.business.data.remote.BusinessApiService
import com.koru.capital.business.domain.Business
import com.koru.capital.business.domain.BusinessRepository
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
) : BusinessRepository {

    override suspend fun addBusiness(business: Business): Result<Unit> {
        val requestDto = business.toAddBusinessRequestDto()
        return try {
            val response = apiService.addBusiness(requestDto)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Error al agregar negocio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}