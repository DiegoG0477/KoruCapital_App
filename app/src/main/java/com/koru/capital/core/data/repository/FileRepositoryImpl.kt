package com.koru.capital.core.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.koru.capital.core.data.remote.StorageApiService // Necesario para uploadFile original
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
import java.io.IOException // Importar IOException
import javax.inject.Inject
import javax.inject.Singleton // Añadir Singleton

@Singleton // Marcar como Singleton
class FileRepositoryImpl @Inject constructor(
    // StorageApiService puede ser opcional si solo se usa para la subida genérica
    private val storageApiService: StorageApiService,
    @ApplicationContext private val context: Context
) : FileRepository {

    // Método uploadFile original (si se usa en otro lado, si no, se puede quitar)
    override suspend fun uploadFile(fileUri: Uri, destinationPath: String?): Result<String> = withContext(Dispatchers.IO) {
        try {
            val tempFile = createTempFileFromUri(context, fileUri)
                ?: return@withContext Result.failure(Exception("Failed to create temporary file from Uri"))
            val requestFile = tempFile.asRequestBody(context.contentResolver.getType(fileUri)?.toMediaTypeOrNull())
            // Asegurarse que el nombre de la parte sea 'file' como espera la API de StorageApiService
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
            val pathPart = destinationPath?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = storageApiService.uploadFile(body, pathPart)
            tempFile.delete() // Limpiar siempre el temporal

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

    // --- IMPLEMENTACIÓN HELPERS MULTIPART ---
    override suspend fun createMultipartBodyPart(fileUri: Uri, partName: String): MultipartBody.Part? = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        var tempFile: File? = null // Declarar fuera para poder borrar en finally
        try {
            // Obtener nombre y tipo MIME
            val fileName = getFileName(context, fileUri) ?: "upload_${System.currentTimeMillis()}"
            val mimeType = contentResolver.getType(fileUri)

            // Crear archivo temporal
            tempFile = createTempFileFromUri(context, fileUri)
            if (tempFile == null) {
                println("Error: Failed to create temporary file from Uri: $fileUri")
                return@withContext null
            }

            // Crear RequestBody y MultipartBody.Part
            val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())
            return@withContext MultipartBody.Part.createFormData(partName, fileName, requestFile)

        } catch (e: Exception) {
            println("Error creating MultipartBody.Part: ${e.message}")
            e.printStackTrace()
            return@withContext null
        } finally {
            // Asegurarse de borrar el archivo temporal incluso si hay error antes de crear la Part
            // Nota: Si la subida falla después de crear la Part, Retrofit maneja el flujo,
            // no necesitamos borrar explícitamente aquí porque el archivo ya no se necesita.
            // El problema es si createTempFileFromUri falla y devuelve null.
            // tempFile?.delete() // No necesitamos borrar aquí, createTempFileFromUri ya es seguro
        }
    }

    override fun createRequestBody(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
        // O usar "multipart/form-data" si la API es muy estricta, pero text/plain suele funcionar
        // return text.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }
    // --- FIN HELPERS ---


    // createTempFileFromUri y getFileName (helpers privados, sin cambios)
    private fun createTempFileFromUri(context: Context, uri: Uri): File? {
        // ... (implementación sin cambios) ...
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileName(context, uri) ?: "temp_upload_${System.currentTimeMillis()}"
            // Usar subdirectorio específico en caché para evitar colisiones
            val cacheDir = File(context.cacheDir, "temp_uploads").apply { mkdirs() }
            val file = File(cacheDir, fileName)
            // Borrar si ya existe uno con el mismo nombre (poco probable con timestamp)
            if (file.exists()) file.delete()
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }
            // Importante: Devolver el archivo para que createMultipartBodyPart lo use
            file
        } catch (e: IOException) { // Ser más específico con la excepción
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
        // ... (implementación sin cambios) ...
        var result: String? = null
        if (uri.scheme == "content") {
            try { // Añadir try-catch por si query falla
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
        // Sanitize filename if necessary (reemplazar caracteres inválidos)
        return result?.replace(Regex("[^a-zA-Z0-9._-]"), "_")?.take(100) // Limitar longitud también?
    }
}