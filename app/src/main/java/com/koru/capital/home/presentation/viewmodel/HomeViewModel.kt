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
    val activeFilters: Set<FilterType> = emptySet(), // Still useful to show active buttons

    // --- Specific Filter Values ---
    val selectedCategoryId: String? = null, // Example: Store selected category ID
    val selectedMaxInvestment: Int? = null, // Example: If MORE_FILTERS sets max investment
    // Add other specific filter values as needed (minProfit, locationId, etc.)
    // val selectedLocationId: String? = null,
    // val selectedMinProfit: Double? = null,

    // --- Pagination State ---
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true,
    val endReached: Boolean = false
)

// BusinessCardUiModel remains the same (defined likely in the same file or imported)
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
// UiState defined above with added fields (selectedCategoryId, selectedMaxInvestment)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBusinessesUseCase: GetBusinessesUseCase,
    private val toggleSaveBusinessUseCase: ToggleSaveBusinessUseCase,
    private val toggleLikeBusinessUseCase: ToggleLikeBusinessUseCase
    // Inject UseCases needed for fetching categories/locations for filter dialogs if applicable
    // private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    companion object {
        private const val PAGE_LIMIT = 15
    }

    init {
        loadBusinesses(loadInitial = true)
        // Load categories/locations for filter dialogs if needed
        // loadFilterOptions()
    }

    // Load data for filter dialogs (Example)
    /*
    private fun loadFilterOptions() {
        viewModelScope.launch {
            // Example: Load categories
             getCategoriesUseCase().collect { categories ->
                 // Store categories in a separate state or update uiState if needed for dialog
             }
        }
    }
    */

    fun loadInitialOrRefreshBusinesses() { // Removed filters param, uses state directly
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                businesses = emptyList(),
                currentPage = 1,
                canLoadMore = true,
                endReached = false
                // Filters (activeFilters, selectedCategoryId, etc.) remain as they are
            )
        }
        loadBusinesses(loadInitial = true) // Load page 1 with current state filters
    }

    fun loadMoreBusinesses() {
        if (_uiState.value.isLoadingMore || _uiState.value.endReached) {
            return
        }
        loadBusinesses(loadInitial = false)
    }

    private fun loadBusinesses(loadInitial: Boolean) {
        fetchJob?.cancel()

        val currentState = _uiState.value // Get current state values
        val pageToLoad = if (loadInitial) 1 else currentState.currentPage

        fetchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingInitial = loadInitial,
                    isLoadingMore = !loadInitial,
                    errorMessage = null
                )
            }

            // Call UseCase with specific filter values from the state
            val result = getBusinessesUseCase(
                page = pageToLoad,
                limit = PAGE_LIMIT,
                selectedCategoryId = currentState.selectedCategoryId,
                maxInvestment = currentState.selectedMaxInvestment,
                isNearby = currentState.activeFilters.contains(FilterType.NEAR_ME),
                // Pass other filters from state if added
                // otherFilters = mapOf("minProfit" to currentState.selectedMinProfit.toString())
            )

            result.fold(
                onSuccess = { paginatedData ->
                    _uiState.update { current -> // Use 'current' for clarity
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

    // --- Filter Handling ---

    fun toggleSimpleFilter(filter: FilterType) {
        // Handle simple toggle filters like NEAR_ME
        val currentFilters = _uiState.value.activeFilters
        val newFilters = if (currentFilters.contains(filter)) {
            currentFilters - filter
        } else {
            currentFilters + filter
        }
        // Update activeFilters set and trigger refresh
        _uiState.update { it.copy(activeFilters = newFilters) }
        loadInitialOrRefreshBusinesses()
    }

    fun onCategoryFilterClicked() {
        // Logic to show a Category selection dialog/screen
        // This dialog would likely fetch categories (if not loaded)
        // When a category is selected in the dialog, call onCategorySelected
        _uiState.update { it.copy(activeFilters = it.activeFilters + FilterType.CATEGORY) } // Keep button active
        // Trigger dialog display (e.g., via another state variable like showCategoryDialog = true)
        println("Category filter clicked - Implement dialog/selection logic")
    }

    fun onMoreFiltersClicked() {
        // Logic to show the "More Filters" dialog/screen
        // This dialog allows setting values like maxInvestment, location, etc.
        // When filters are applied in the dialog, call onApplyMoreFilters
        _uiState.update { it.copy(activeFilters = it.activeFilters + FilterType.MORE_FILTERS) } // Keep button active
        // Trigger dialog display (e.g., showMoreFiltersDialog = true)
        println("More Filters clicked - Implement dialog/selection logic")
    }


    // Called when a category IS selected from the dialog/screen
    fun onCategorySelected(categoryId: String?) {
        // Update the specific category ID state and trigger refresh
        _uiState.update {
            val updatedFilters = if (categoryId == null) it.activeFilters - FilterType.CATEGORY else it.activeFilters // Deactivate button if category cleared
            it.copy(selectedCategoryId = categoryId, activeFilters = updatedFilters)
        }
        loadInitialOrRefreshBusinesses()
        // Hide category dialog if shown via state
    }

    // Called when "Apply" is clicked in the "More Filters" dialog
    fun onApplyMoreFilters(maxInvestment: Int?, /* other filter values */ ) {
        // Update specific filter states and trigger refresh
        val hasSpecificFilters = maxInvestment != null // Add checks for other filters
        _uiState.update {
            val updatedFilters = if (!hasSpecificFilters) it.activeFilters - FilterType.MORE_FILTERS else it.activeFilters // Deactivate button if all specific filters cleared
            it.copy(
                selectedMaxInvestment = maxInvestment,
                // Update other filter states...
                activeFilters = updatedFilters
            )
        }
        loadInitialOrRefreshBusinesses()
        // Hide more filters dialog
    }

    // Central function called by UI FilterRow
    fun handleFilterClick(filterType: FilterType) {
        when(filterType) {
            FilterType.NEAR_ME -> toggleSimpleFilter(filterType) // Simple toggle
            FilterType.LESS_THAN_50K -> { // Example of handling a filter that sets a specific value
                val currentMax = _uiState.value.selectedMaxInvestment
                val isActive = _uiState.value.activeFilters.contains(filterType)
                val newMax = if (isActive) null else 50000 // Toggle 50k value
                val newFilters = if (isActive) {
                    _uiState.value.activeFilters - filterType
                } else {
                    _uiState.value.activeFilters + filterType // Assumes only one investment filter active at a time
                }
                _uiState.update { it.copy(selectedMaxInvestment = newMax, activeFilters = newFilters)}
                loadInitialOrRefreshBusinesses()
            }
            FilterType.CATEGORY -> onCategoryFilterClicked() // Opens category selection
            FilterType.MORE_FILTERS -> onMoreFiltersClicked() // Opens more filters dialog
        }
    }


    // toggleSaveBusiness and toggleLikeBusiness remain the same
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
            }.filter { (it.savedCount ?: 0) >= 0 } // Ensure count doesn't go negative visually
            currentState.copy(businesses = updatedBusinesses, errorMessage = null)
        }
        viewModelScope.launch {
            val result = toggleSaveBusinessUseCase(businessId)
            result.onFailure { exception ->
                // Revert UI on failure and show error
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
                // Revert UI on failure and show error
                _uiState.update { originalState.copy(errorMessage = "Error en 'Me Gusta': ${exception.message}") }
            }
        }
    }
}