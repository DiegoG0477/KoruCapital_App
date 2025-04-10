package com.koru.capital.core.domain.repository

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Define operaciones relacionadas con archivos, incluyendo subida y
 * preparación para peticiones multipart.
 */
interface FileRepository {
    /**
     * Sube un archivo representado por una Uri local (si se usa para otras features).
     * @param fileUri The Uri of the file to upload.
     * @param destinationPath Optional path/folder in the storage service (e.g., "business_images/").
     * @return Result containing the public URL of the uploaded file on success, or Exception on failure.
     */
    suspend fun uploadFile(fileUri: Uri, destinationPath: String? = null): Result<String>

    // --- HELPERS PARA MULTIPART ---
    /**
     * Convierte una Uri de archivo (imagen, etc.) a un [MultipartBody.Part] listo para Retrofit.
     * Se encarga de obtener el tipo MIME y crear un archivo temporal si es necesario.
     * @param fileUri La Uri del archivo local.
     * @param partName El nombre del campo esperado por la API para este archivo (ej: "profileImage", "imageUrl").
     * @return El [MultipartBody.Part] o null si ocurre un error al procesar la Uri.
     */
    suspend fun createMultipartBodyPart(fileUri: Uri, partName: String): MultipartBody.Part?

    /**
     * Convierte un String simple a un [RequestBody] para ser enviado como
     * parte de texto en una petición multipart.
     * @param text El texto a convertir.
     * @return El [RequestBody] correspondiente.
     */
    fun createRequestBody(text: String): RequestBody
}