package com.koru.capital.business.domain.usecase

import android.net.Uri
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.core.domain.repository.FileRepository // Import FileRepo
import javax.inject.Inject

class AddBusinessUseCase @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val fileRepository: FileRepository // Inyectar FileRepo
    // Ya no se necesita UploadFileUseCase aquí
) {
    /**
     * Orquesta la creación de un negocio, preparando datos para multipart.
     * @param ownerId ID del usuario propietario.
     * @param businessData Datos básicos del negocio (sin ID).
     * @param imageUri Uri opcional de la imagen a subir.
     * @return Result con el Business creado.
     */
    suspend operator fun invoke(
        ownerId: String, // Necesitamos el ownerId
        businessData: Business, // Recibe el modelo de dominio
        imageUri: Uri?
    ): Result<Business> { // Devuelve el Business creado

        try {
            // 1. Crear RequestBody para campos de texto
            val ownerIdPart = fileRepository.createRequestBody(ownerId)
            val namePart = fileRepository.createRequestBody(businessData.name)
            val descPart = fileRepository.createRequestBody(businessData.description)
            val investPart = fileRepository.createRequestBody(businessData.investment.toString())
            val profitPart = fileRepository.createRequestBody(businessData.profitPercentage.toString())
            val categoryPart = fileRepository.createRequestBody(businessData.categoryId.toString())
            val municipalityPart = fileRepository.createRequestBody(businessData.municipalityId.toString()) // Enviar ID como string o num? API espera num, pero DTO era Int... Enviar como String por simplicidad multipart
            val modelPart = fileRepository.createRequestBody(businessData.businessModel)
            val incomePart = fileRepository.createRequestBody(businessData.monthlyIncome.toString())

            // 2. Crear MultipartBody.Part para la imagen (si existe)
            val imagePart: okhttp3.MultipartBody.Part? = imageUri?.let { uri ->
                // Usar "imageUrl" como nombre de parte, como espera la API
                fileRepository.createMultipartBodyPart(uri, "imageUrl")
                    ?: return Result.failure(Exception("Failed to prepare image for upload."))
            }

            // 3. Llamar al repositorio con las partes
            return businessRepository.addBusiness(
                ownerId = ownerIdPart,
                name = namePart,
                description = descPart,
                investment = investPart,
                profitPercentage = profitPart,
                categoryId = categoryPart,
                municipalityId = municipalityPart,
                businessModel = modelPart,
                monthlyIncome = incomePart,
                imageUrl = imagePart
            )
        } catch (e: Exception) {
            println("Error in AddBusinessUseCase preparing parts: ${e.message}")
            return Result.failure(Exception("Error preparing business data: ${e.message}", e))
        }
    }
}