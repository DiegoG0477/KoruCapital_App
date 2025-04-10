package com.koru.capital.profile.domain.repository

import com.koru.capital.profile.domain.model.UserProfile
// Quitar import de UserProfileUpdate
import okhttp3.MultipartBody // Importar
import okhttp3.RequestBody // Importar

interface ProfileRepository {
    suspend fun getMyProfile(): Result<UserProfile>

    /**
     * Actualiza el perfil enviando datos como multipart.
     * @param firstName RequestBody para el nombre.
     * @param lastName RequestBody para el apellido.
     * @param biography RequestBody opcional para la bio.
     * @param linkedinProfile RequestBody opcional para LinkedIn.
     * @param instagramHandle RequestBody opcional para Instagram.
     * @param profileImage MultipartBody.Part opcional para la imagen.
     * @return Result con el UserProfile actualizado.
     */
    suspend fun updateMyProfile(
        firstName: RequestBody,
        lastName: RequestBody,
        biography: RequestBody?,
        linkedinProfile: RequestBody?,
        instagramHandle: RequestBody?,
        profileImage: MultipartBody.Part?
    ): Result<UserProfile>
}