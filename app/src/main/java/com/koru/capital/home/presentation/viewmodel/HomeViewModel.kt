package com.koru.capital.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.home.data.mapper.toUiModel
import com.koru.capital.home.domain.usecase.GetBusinessesUseCase
import com.koru.capital.home.domain.usecase.ToggleLikeBusinessUseCase
import com.koru.capital.home.domain.usecase.ToggleSaveBusinessUseCase
import com.koru.capital.home.presentation.ui.components.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map
import kotlin.collections.mapNotNull

data class HomeUiState(
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val businesses: List<BusinessCardUiModel> = emptyList(),
    val activeFilters: Set<FilterType> = emptySet(),

    val selectedCategoryId: String? = null,
    val selectedMaxInvestment: Int? = null,

    val currentPage: Int = 1,
    val canLoadMore: Boolean = true,
    val endReached: Boolean = false
)

data class BusinessCardUiModel(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val category: String?,
    val location: String?,
    val investmentRange: String?,
    val partnerCount: Int?,
    val description: String,
    val businessModel: String,
    val ownerName: String?,
    val ownerImageUrl: String?,
    val savedCount: Int?,
    val isSaved: Boolean,
    val isLiked: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBusinessesUseCase: GetBusinessesUseCase,
    private val toggleSaveBusinessUseCase: ToggleSaveBusinessUseCase,
    private val toggleLikeBusinessUseCase: ToggleLikeBusinessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    companion object {
        private const val PAGE_LIMIT = 15
    }

    init {
        loadBusinesses(loadInitial = true)
    }


    fun loadInitialOrRefreshBusinesses() {
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                businesses = emptyList(),
                currentPage = 1,
                canLoadMore = true,
                endReached = false
            )
        }
        loadBusinesses(loadInitial = true)
    }

    fun loadMoreBusinesses() {
        if (_uiState.value.isLoadingMore || _uiState.value.endReached) {
            return
        }
        loadBusinesses(loadInitial = false)
    }

    private fun loadBusinesses(loadInitial: Boolean) {
        fetchJob?.cancel()

        val currentState = _uiState.value
        val pageToLoad = if (loadInitial) 1 else currentState.currentPage

        fetchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingInitial = loadInitial,
                    isLoadingMore = !loadInitial,
                    errorMessage = null
                )
            }

            val result = getBusinessesUseCase(
                page = pageToLoad,
                limit = PAGE_LIMIT,
                selectedCategoryId = currentState.selectedCategoryId,
                maxInvestment = currentState.selectedMaxInvestment,
                isNearby = currentState.activeFilters.contains(FilterType.NEAR_ME),
            )

            result.fold(
                onSuccess = { paginatedData ->
                    _uiState.update { current ->
                        current.copy(
                            isLoadingInitial = false,
                            isLoadingMore = false,
                            businesses = if (loadInitial) paginatedData.items.map { it.toUiModel() }
                            else current.businesses + paginatedData.items.map { it.toUiModel() },
                            currentPage = paginatedData.nextPage ?: current.currentPage,
                            canLoadMore = paginatedData.hasMore,
                            endReached = !paginatedData.hasMore
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoadingInitial = false,
                            isLoadingMore = false,
                            errorMessage = "Error al cargar: ${exception.message}"
                        )
                    }
                }
            )
        }
    }


    fun toggleSimpleFilter(filter: FilterType) {
        val currentFilters = _uiState.value.activeFilters
        val newFilters = if (currentFilters.contains(filter)) {
            currentFilters - filter
        } else {
            currentFilters + filter
        }
        _uiState.update { it.copy(activeFilters = newFilters) }
        loadInitialOrRefreshBusinesses()
    }

    fun onCategoryFilterClicked() {
        _uiState.update { it.copy(activeFilters = it.activeFilters + FilterType.CATEGORY) }
        println("Category filter clicked - Implement dialog/selection logic")
    }

    fun onMoreFiltersClicked() {
        _uiState.update { it.copy(activeFilters = it.activeFilters + FilterType.MORE_FILTERS) }
        println("More Filters clicked - Implement dialog/selection logic")
    }


    fun onCategorySelected(categoryId: String?) {
        _uiState.update {
            val updatedFilters = if (categoryId == null) it.activeFilters - FilterType.CATEGORY else it.activeFilters
            it.copy(selectedCategoryId = categoryId, activeFilters = updatedFilters)
        }
        loadInitialOrRefreshBusinesses()
    }

    fun onApplyMoreFilters(maxInvestment: Int?,  ) {
        val hasSpecificFilters = maxInvestment != null
        _uiState.update {
            val updatedFilters = if (!hasSpecificFilters) it.activeFilters - FilterType.MORE_FILTERS else it.activeFilters
            it.copy(
                selectedMaxInvestment = maxInvestment,
                activeFilters = updatedFilters
            )
        }
        loadInitialOrRefreshBusinesses()
    }

    fun handleFilterClick(filterType: FilterType) {
        when(filterType) {
            FilterType.NEAR_ME -> toggleSimpleFilter(filterType)
            FilterType.LESS_THAN_50K -> {
                val currentMax = _uiState.value.selectedMaxInvestment
                val isActive = _uiState.value.activeFilters.contains(filterType)
                val newMax = if (isActive) null else 50000
                val newFilters = if (isActive) {
                    _uiState.value.activeFilters - filterType
                } else {
                    _uiState.value.activeFilters + filterType
                }
                _uiState.update { it.copy(selectedMaxInvestment = newMax, activeFilters = newFilters)}
                loadInitialOrRefreshBusinesses()
            }
            FilterType.CATEGORY -> onCategoryFilterClicked()
            FilterType.MORE_FILTERS -> onMoreFiltersClicked()
        }
    }


    fun toggleSaveBusiness(businessId: String) {
        val originalState = _uiState.value
        _uiState.update { currentState ->
            val updatedBusinesses = currentState.businesses.mapNotNull { business ->
                if (business.id == businessId) {
                    business.copy(
                        isSaved = !business.isSaved,
                        savedCount = if (business.isSaved) (business.savedCount ?: 1) - 1 else (business.savedCount ?: 0) + 1
                    )
                } else { business }
            }.filter { (it.savedCount ?: 0) >= 0 }
            currentState.copy(businesses = updatedBusinesses, errorMessage = null)
        }
        viewModelScope.launch {
            val result = toggleSaveBusinessUseCase(businessId)
            result.onFailure { exception ->
                _uiState.update { originalState.copy(errorMessage = "Error al guardar: ${exception.message}") }
            }
        }
    }

    fun toggleLikeBusiness(businessId: String) {
        val originalState = _uiState.value
        _uiState.update { currentState ->
            val updatedBusinesses = currentState.businesses.map { business ->
                if (business.id == businessId) { business.copy(isLiked = !business.isLiked) } else { business }
            }
            currentState.copy(businesses = updatedBusinesses, errorMessage = null)
        }
        viewModelScope.launch {
            val result = toggleLikeBusinessUseCase(businessId)
            result.onFailure { exception ->
                _uiState.update { originalState.copy(errorMessage = "Error en 'Me Gusta': ${exception.message}") }
            }
        }
    }
}