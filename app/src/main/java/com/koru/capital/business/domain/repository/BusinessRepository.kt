package com.koru.capital.business.domain.repository

import com.koru.capital.business.domain.model.Business // Importar modelo de dominio
import com.koru.capital.business.presentation.viewmodel.BusinessFilter // Usar el enum de la app
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Contrato para el repositorio de la feature Business.
 */
interface BusinessRepository {

    /**
     * Crea un nuevo negocio enviando datos como multipart.
     * @param ownerId RequestBody del ID del dueño.
     * @param name RequestBody del nombre.
     * ... otros campos ...
     * @param imageUrl Part opcional para la imagen.
     * @return Result con el Business creado.
     */
    suspend fun addBusiness(
        ownerId: RequestBody,
        name: RequestBody,
        description: RequestBody,
        investment: RequestBody,
        profitPercentage: RequestBody,
        categoryId: RequestBody,
        municipalityId: RequestBody,
        businessModel: RequestBody,
        monthlyIncome: RequestBody,
        imageUrl: MultipartBody.Part?
    ): Result<Business> // Devolver el Business creado

    /**
     * Obtiene detalles de un negocio por ID.
     * @param businessId ID del negocio.
     * @return Result con el Business o error.
     */
    suspend fun getBusinessDetails(businessId: String): Result<Business>

    /**
     * Actualiza un negocio enviando datos como multipart.
     * @param businessId ID del negocio a actualizar.
     * ... otros campos opcionales como RequestBody? ...
     * @param imageUrl Part opcional para la imagen.
     * @return Result con el Business actualizado.
     */
    suspend fun updateBusiness(
        businessId: String,
        name: RequestBody?,
        description: RequestBody?,
        investment: RequestBody?,
        profitPercentage: RequestBody?,
        categoryId: RequestBody?,
        municipalityId: RequestBody?,
        businessModel: RequestBody?,
        monthlyIncome: RequestBody?,
        imageUrl: MultipartBody.Part?
    ): Result<Business> // Devolver el Business actualizado

    /**
     * Elimina un negocio.
     * @param businessId ID del negocio a eliminar.
     * @return Result Unit en éxito o Exception en fallo.
     */
    suspend fun deleteBusiness(businessId: String): Result<Unit> // <-- NUEVO MÉTODO

    /**
     * Obtiene la lista de negocios del usuario según el filtro.
     * @param filter El filtro a aplicar (OWNED, SAVED, PARTNERED).
     * @return Result con la lista de BusinessListItemUiModel o error.
     */
    // Usaremos directamente el UI Model aquí por simplicidad,
    // aunque estrictamente debería devolver List<Business> y mapear en ViewModel
    suspend fun getMyBusinesses(filter: BusinessFilter): Result<List<com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel>> // <-- NUEVO MÉTODO
}