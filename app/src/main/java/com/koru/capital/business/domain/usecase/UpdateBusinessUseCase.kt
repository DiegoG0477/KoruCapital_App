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
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        businessId: String,
        businessData: Business,
        newImageUri: Uri?
    ): Result<Business> {


        try {
            val namePart: RequestBody? = businessData.name.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val descPart: RequestBody? = businessData.description.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val investPart: RequestBody? = fileRepository.createRequestBody(businessData.investment.toString())
            val profitPart: RequestBody? = fileRepository.createRequestBody(businessData.profitPercentage.toString())
            val categoryPart: RequestBody? = fileRepository.createRequestBody(businessData.categoryId.toString())
            val municipalityPart: RequestBody? = fileRepository.createRequestBody(businessData.municipalityId)
            val modelPart: RequestBody? = businessData.businessModel.takeIf { it.isNotBlank() }?.let { fileRepository.createRequestBody(it) }
            val incomePart: RequestBody? = fileRepository.createRequestBody(businessData.monthlyIncome.toString())

            val imagePart: MultipartBody.Part? = newImageUri?.let { uri ->
                fileRepository.createMultipartBodyPart(uri, "imageUrl")
                    ?: return Result.failure(Exception("Failed to prepare new image for upload."))
            }

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
                imageUrl = imagePart
            )

        } catch (e: Exception) {
            println("Error in UpdateBusinessUseCase preparing parts: ${e.message}")
            return Result.failure(Exception("Error preparing business update data: ${e.message}", e))
        }
    }
}