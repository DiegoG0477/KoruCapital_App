package com.koru.capital.core.domain.usecase

import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<List<State>> = flow {
        emit(repository.getStates())
    }
}