package com.koru.capital.core.domain.repository

import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State

/**
 * Contrato para obtener datos de ubicaciones y categorías desde cualquier fuente de datos.
 */
interface LocationRepository {
    /**
     * Obtiene la lista de todos los países disponibles.
     * @return Un [Result] que contiene la lista de [Country] en caso de éxito,
     * o una [Exception] en caso de fallo.
     */
    suspend fun getCountries(): Result<List<Country>>

    /**
     * Obtiene la lista de estados/provincias filtrados por el ID del país.
     * @param countryId El ID del país.
     * @return Un [Result] que contiene la lista de [State] en caso de éxito,
     * o una [Exception] en caso de fallo.
     */
    suspend fun getStatesByCountry(countryId: String): Result<List<State>>

    /**
     * Obtiene la lista de municipios filtrados por el ID del estado.
     * @param stateId El ID del estado.
     * @return Un [Result] que contiene la lista de [Municipality] en caso de éxito,
     * o una [Exception] en caso de fallo.
     */
    suspend fun getMunicipalitiesByState(stateId: String): Result<List<Municipality>>

    /**
     * Obtiene la lista de todas las categorías de negocio.
     * @return Un [Result] que contiene la lista de [Category] en caso de éxito,
     * o una [Exception] en caso de fallo.
     */
    suspend fun getCategories(): Result<List<Category>>
}