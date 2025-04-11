package com.koru.capital.core.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class FileUploadResponseDto(
    val success: Boolean,
    val message: String,
    val fileUrl: String?
)

interface StorageApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("path") path: RequestBody? = null
    ): Response<FileUploadResponseDto>
}