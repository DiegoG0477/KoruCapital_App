package com.koru.capital.core.data.mapper

import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.CountryDto // <-- Importado
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country // <-- Importado
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State

// --- Mappers de DTO a Dominio ---

fun CountryDto.toDomain(): Country {
    return Country(
        id = this.id,
        name = this.name
    )
}

fun StateDto.toDomain(): State {
    // Asumimos que el modelo de dominio State no necesita countryId por ahora
    return State(
        id = this.id,
        name = this.name
    )
}

fun MunicipalityDto.toDomain(): Municipality {
    return Municipality(
        id = this.id,
        name = this.name,
        stateId = this.stateId // El DTO y el Modelo lo tienen
    )
}

fun CategoryDto.toDomain(): Category {
    // La API devuelve iconKey, el DTO de Android lo llama 'icon'
    return Category(
        id = this.id, // Asumimos que Gson maneja conversión Numérico (API) -> String (DTO) si es necesario
        name = this.name,
        iconKey = this.icon ?: "" // Mapear 'icon' del DTO a 'iconKey' del dominio
    )
}