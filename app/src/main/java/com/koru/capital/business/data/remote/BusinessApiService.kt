package com.koru.capital.business.data.remote

import com.koru.capital.business.data.dto.AddBusinessRequestDto
import com.koru.capital.core.data.dto.ResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BusinessApiService {
    @POST("businesses/add")
    suspend fun addBusiness(
        @Body request: AddBusinessRequestDto
    ): Response<ResponseDto>
}