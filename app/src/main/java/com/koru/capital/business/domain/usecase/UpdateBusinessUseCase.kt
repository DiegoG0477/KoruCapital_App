package com.koru.capital.business.domain.usecase

import android.net.Uri
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.core.domain.repository.FileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateBusinessUseCase @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val fileRepository: FileRepository // Inyectar FileRepo
    // Ya no se necesita UploadFileUseCase aquí
) {
    /**
     * Orquesta la actualización de un negocio, preparando datos para multipart.
     * @param businessId ID del negocio a actualizar.
     * @param businessData Objeto Business con los *nuevos* valores deseados.
     * @param newImageUri Uri opcional de la *nueva* imagen a subir.
     * @return Result con el Business actualizado.
     */
    suspend operator fun invoke(
        businessId: String,
        businessData: Business, // Recibe el objeto con todos los datos actualizados
        newImageUri: Uri?
    ): Result<Business> { // Devuelve el Business actualizado

        // Nota: La autorización (verificar dueño) se delega al repositorio/API

        try {
            // 1. Crear RequestBody para todos los campos (incluso si no cambiaron, API/Repo decide)
            val namePart: RequestBody? = businessData.name.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val descPart: RequestBody? = businessData.description.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val investPart: RequestBody? = fileRepository.createRequestBody(businessData.investment.toString())
            val profitPart: RequestBody? = fileRepository.createRequestBody(businessData.profitPercentage.toString())
            val categoryPart: RequestBody? = fileRepository.createRequestBody(businessData.categoryId.toString())
            val municipalityPart: RequestBody? = fileRepository.createRequestBody(businessData.municipalityId.toString()) // Enviar como String
            val modelPart: RequestBody? = businessData.businessModel.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val incomePart: RequestBody? = fileRepository.createRequestBody(businessData.monthlyIncome.toString())
            // El campo imageUrl en el DTO base no se usa directamente para la imagen subida

            // 2. Crear MultipartBody.Part para la nueva imagen (si existe)
            val imagePart: MultipartBody.Part? = newImageUri?.let { uri ->
                // Nombre de parte "imageUrl" como espera la API
                fileRepository.createMultipartBodyPart(uri, "imageUrl")
                    ?: return Result.failure(Exception("Failed to prepare new image for upload."))
            }
            // Si no hay newImageUri, imagePart será null, y la API no actualizará la imagen
            // a menos que explícitamente enviemos un campo para borrarla (no implementado aquí)

            // 3. Llamar al repositorio
            return businessRepository.updateBusiness(
                businessId = businessId,
                name = namePart,
                description = descPart,
                investment = investPart,
                profitPercentage = profitPart,
                categoryId = categoryPart,
                municipalityId = municipalityPart,
                businessModel = modelPart,
                monthlyIncome = incomePart,
                imageUrl = imagePart // Pasar la parte de imagen (puede ser null)
            )

        } catch (e: Exception) {
            println("Error in UpdateBusinessUseCase preparing parts: ${e.message}")
            return Result.failure(Exception("Error preparing business update data: ${e.message}", e))
        }
    }
}