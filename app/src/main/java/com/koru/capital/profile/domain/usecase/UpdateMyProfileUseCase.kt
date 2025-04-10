package com.koru.capital.profile.domain.usecase

import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.model.UserProfileUpdate
import com.koru.capital.profile.domain.repository.ProfileRepository
import android.net.Uri
import com.koru.capital.core.domain.repository.FileRepository // Necesario para crear RequestBody/Part
import okhttp3.MultipartBody // Importar
import okhttp3.RequestBody // Importar
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val fileRepository: FileRepository // Inyectar para crear partes
    // El UploadFileUseCase genérico ya no es necesario aquí
) {
    /**
     * Executes the profile update use case, sending data as multipart.
     * @param updateData The data containing user's desired changes.
     * @param newProfileImageUri Optional Uri of a new profile image.
     * @return Result containing the updated UserProfile on success.
     */
    suspend operator fun invoke(
        updateData: UserProfileUpdate,
        newProfileImageUri: Uri?
    ): Result<UserProfile> {

        if (updateData.firstName.isBlank() || updateData.lastName.isBlank()) {
            return Result.failure(IllegalArgumentException("Nombre y apellido son requeridos."))
        }

        try {
            // 1. Crear RequestBody para campos de texto obligatorios
            val firstNamePart: RequestBody = fileRepository.createRequestBody(updateData.firstName)
            val lastNamePart: RequestBody = fileRepository.createRequestBody(updateData.lastName)

            // 2. Crear RequestBody para campos opcionales (solo si no son nulos/vacíos)
            val bioPart: RequestBody? = updateData.bio?.takeIf { it.isNotBlank() }
                ?.let { fileRepository.createRequestBody(it) }
            val linkedInPart: RequestBody? = updateData.linkedInUrl?.takeIf { it.isNotBlank() }
                ?.let { fileRepository.createRequestBody(it) }

            // La API espera 'instagramHandle'
            val instagramHandle: String? = updateData.instagramUrl?.substringAfterLast('/', "")
                ?.takeIf { it.isNotBlank() }
            val instagramPart: RequestBody? = instagramHandle?.let { fileRepository.createRequestBody(it) }

            // 3. Crear MultipartBody.Part para la imagen si existe
            val imagePart: MultipartBody.Part? = newProfileImageUri?.let { uri ->
                // Nombre de parte "profileImage" como espera la API/Multer
                fileRepository.createMultipartBodyPart(uri, "profileImage")
                    ?: return Result.failure(Exception("Failed to prepare profile image for upload.")) // Fallar si la preparación falla
            }

            // 4. Llamar al repositorio con todas las partes
            return profileRepository.updateMyProfile(
                firstName = firstNamePart,
                lastName = lastNamePart,
                biography = bioPart,
                linkedinProfile = linkedInPart,
                instagramHandle = instagramPart,
                profileImage = imagePart
            )

        } catch (e: Exception) {
            // Capturar errores durante la creación de partes o llamada al repo
            println("Error in UpdateMyProfileUseCase: ${e.message}")
            e.printStackTrace()
            return Result.failure(Exception("Error preparing profile update: ${e.message}", e))
        }
    }
}