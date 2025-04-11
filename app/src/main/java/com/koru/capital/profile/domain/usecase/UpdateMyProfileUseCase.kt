package com.koru.capital.profile.domain.usecase

import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.model.UserProfileUpdate
import com.koru.capital.profile.domain.repository.ProfileRepository
import android.net.Uri
import com.koru.capital.core.domain.repository.FileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        updateData: UserProfileUpdate,
        newProfileImageUri: Uri?
    ): Result<UserProfile> {

        if (updateData.firstName.isBlank() || updateData.lastName.isBlank()) {
            return Result.failure(IllegalArgumentException("Nombre y apellido son requeridos."))
        }

        try {
            val firstNamePart: RequestBody = fileRepository.createRequestBody(updateData.firstName)
            val lastNamePart: RequestBody = fileRepository.createRequestBody(updateData.lastName)

            val bioPart: RequestBody? = updateData.bio?.takeIf { it.isNotBlank() }
                ?.let { fileRepository.createRequestBody(it) }
            val linkedInPart: RequestBody? = updateData.linkedInUrl?.takeIf { it.isNotBlank() }
                ?.let { fileRepository.createRequestBody(it) }

            val instagramHandle: String? = updateData.instagramUrl?.substringAfterLast('/', "")
                ?.takeIf { it.isNotBlank() }
            val instagramPart: RequestBody? = instagramHandle?.let { fileRepository.createRequestBody(it) }

            val imagePart: MultipartBody.Part? = newProfileImageUri?.let { uri ->
                fileRepository.createMultipartBodyPart(uri, "profileImage")
                    ?: return Result.failure(Exception("Failed to prepare profile image for upload."))
            }

            return profileRepository.updateMyProfile(
                firstName = firstNamePart,
                lastName = lastNamePart,
                biography = bioPart,
                linkedinProfile = linkedInPart,
                instagramHandle = instagramPart,
                profileImage = imagePart
            )

        } catch (e: Exception) {
            println("Error in UpdateMyProfileUseCase: ${e.message}")
            e.printStackTrace()
            return Result.failure(Exception("Error preparing profile update: ${e.message}", e))
        }
    }
}