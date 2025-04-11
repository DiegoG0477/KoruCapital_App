package com.koru.capital.core.domain.repository

import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State


interface LocationRepository {
    suspend fun getCountries(): Result<List<Country>>

    suspend fun getStatesByCountry(countryId: String): Result<List<State>>

    suspend fun getMunicipalitiesByState(stateId: String): Result<List<Municipality>>

    suspend fun getCategories(): Result<List<Category>>
}