package com.koru.capital.core.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.koru.capital.core.data.remote.StorageApiService
import com.koru.capital.core.domain.repository.FileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl @Inject constructor(
    private val storageApiService: StorageApiService,
    @ApplicationContext private val context: Context
) : FileRepository {

    override suspend fun uploadFile(fileUri: Uri, destinationPath: String?): Result<String> = withContext(Dispatchers.IO) {
        try {
            val tempFile = createTempFileFromUri(context, fileUri)
                ?: return@withContext Result.failure(Exception("Failed to create temporary file from Uri"))
            val requestFile = tempFile.asRequestBody(context.contentResolver.getType(fileUri)?.toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
            val pathPart = destinationPath?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = storageApiService.uploadFile(body, pathPart)
            tempFile.delete()

            if (response.isSuccessful && response.body() != null && response.body()!!.success && response.body()!!.fileUrl != null) {
                Result.success(response.body()!!.fileUrl!!)
            } else {
                val errorMsg = response.body()?.message ?: "File upload via StorageApiService failed (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("File upload error: ${e.message}", e))
        }
    }

    override suspend fun createMultipartBodyPart(fileUri: Uri, partName: String): MultipartBody.Part? = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        var tempFile: File? = null
        try {
            val fileName = getFileName(context, fileUri) ?: "upload_${System.currentTimeMillis()}"
            val mimeType = contentResolver.getType(fileUri)

            tempFile = createTempFileFromUri(context, fileUri)
            if (tempFile == null) {
                println("Error: Failed to create temporary file from Uri: $fileUri")
                return@withContext null
            }

            val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())
            return@withContext MultipartBody.Part.createFormData(partName, fileName, requestFile)

        } catch (e: Exception) {
            println("Error creating MultipartBody.Part: ${e.message}")
            e.printStackTrace()
            return@withContext null
        } finally {
        }
    }

    override fun createRequestBody(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    private fun createTempFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileName(context, uri) ?: "temp_upload_${System.currentTimeMillis()}"
            val cacheDir = File(context.cacheDir, "temp_uploads").apply { mkdirs() }
            val file = File(cacheDir, fileName)
            if (file.exists()) file.delete()
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }
            file
        } catch (e: IOException) {
            println("IOException creating temp file: ${e.message}")
            e.printStackTrace()
            null
        } catch (e: SecurityException) {
            println("SecurityException accessing URI: ${e.message}")
            e.printStackTrace()
            null
        } catch (e: Exception) {
            println("Generic error creating temp file: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            try {
                context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (columnIndex != -1) {
                            result = cursor.getString(columnIndex)
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error querying filename: ${e.message}")
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result?.replace(Regex("[^a-zA-Z0-9._-]"), "_")?.take(100)
    }
}