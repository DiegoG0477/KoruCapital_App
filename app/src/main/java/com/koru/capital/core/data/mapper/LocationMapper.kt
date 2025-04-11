package com.koru.capital.core.data.mapper

import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.CountryDto
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State


fun CountryDto.toDomain(): Country {
    return Country(
        id = this.id,
        name = this.name
    )
}

fun StateDto.toDomain(): State {
    return State(
        id = this.id,
        name = this.name
    )
}

fun MunicipalityDto.toDomain(): Municipality {
    return Municipality(
        id = this.id,
        name = this.name,
        stateId = this.stateId
    )
}

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        iconKey = this.icon ?: ""
    )
}