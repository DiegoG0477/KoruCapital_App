package com.koru.capital.core.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// Example DTO for upload response
data class FileUploadResponseDto(
    val success: Boolean,
    val message: String,
    val fileUrl: String?
)

interface StorageApiService {
    @Multipart
    @POST("upload") // Your backend's upload endpoint
    suspend fun uploadFile(
        @Part file: MultipartBody.Part, // The actual file content
        @Part("path") path: RequestBody? = null // Optional destination path as RequestBody
        // Add other parts if needed (e.g., userId, type)
    ): Response<FileUploadResponseDto>
}