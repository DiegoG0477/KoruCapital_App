package com.koru.capital.business.domain

interface BusinessRepository {
    suspend fun addBusiness(business: Business): Result<Unit>
}