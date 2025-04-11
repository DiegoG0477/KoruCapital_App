package com.koru.capital.core.domain.repository

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface FileRepository {
    suspend fun uploadFile(fileUri: Uri, destinationPath: String? = null): Result<String>

    suspend fun createMultipartBodyPart(fileUri: Uri, partName: String): MultipartBody.Part?

    fun createRequestBody(text: String): RequestBody
}