package com.koru.capital.business.domain.usecase

import android.net.Uri
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.core.domain.repository.FileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddBusinessUseCase @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        businessData: Business,
        imageUri: Uri?
    ): Result<Business> {

        if (businessData.name.isBlank() || businessData.description.isBlank() ) {
            return Result.failure(IllegalArgumentException("Datos básicos del negocio incompletos."))
        }

        try {
            val ownerIdPart = fileRepository.createRequestBody(ownerId)
            val namePart = fileRepository.createRequestBody(businessData.name)
            val descPart = fileRepository.createRequestBody(businessData.description)
            val investPart = fileRepository.createRequestBody(businessData.investment.toString())
            val profitPart = fileRepository.createRequestBody(businessData.profitPercentage.toString())
            val categoryPart = fileRepository.createRequestBody(businessData.categoryId.toString())
            val municipalityPart = fileRepository.createRequestBody(businessData.municipalityId)
            val modelPart = fileRepository.createRequestBody(businessData.businessModel)
            val incomePart = fileRepository.createRequestBody(businessData.monthlyIncome.toString())

            val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                fileRepository.createMultipartBodyPart(uri, "imageUrl")
                    ?: return Result.failure(Exception("No se pudo preparar la imagen para subirla."))
            }

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
            e.printStackTrace()
            return Result.failure(Exception("Error al preparar datos del negocio: ${e.message}", e))
        }
    }
}