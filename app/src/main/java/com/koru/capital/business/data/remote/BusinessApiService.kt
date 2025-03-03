package com.koru.capital.business.data.remote

import com.koru.capital.business.data.remote.dto.AddBusinessRequestDto
import com.koru.capital.core.ui.data.dto.ResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BusinessApiService {
    @POST("businesses/add")
    suspend fun addBusiness(
        @Body request: AddBusinessRequestDto
    ): Response<ResponseDto>
}