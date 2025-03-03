package com.koru.capital.core.data.mapper

import com.koru.capital.core.data.dto.CategoryDto
import com.koru.capital.core.data.dto.MunicipalityDto
import com.koru.capital.core.data.dto.StateDto
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        iconKey = icon  // Aquí puedes mapear el string a un ImageVector en la capa de UI si es necesario
    )
}

fun StateDto.toDomain(): State {
    return State(
        id = id,
        name = name
    )
}

fun MunicipalityDto.toDomain(): Municipality {
    return Municipality(
        id = id,
        name = name,
        stateId = stateId
    )
}