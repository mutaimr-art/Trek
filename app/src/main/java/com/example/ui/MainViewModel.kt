package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Bike
import com.example.data.BikeCategory
import com.example.data.BikeRepository
import com.example.data.NegotiationOffer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    CATALOG,
    NEGOTIATE,
    SAVED,
    STORE
}

data class CatalogUiState(
    val bikes: List<Bike> = emptyList(),
    val selectedCategory: BikeCategory = BikeCategory.ALL,
    val searchQuery: String = "",
    val favoriteIds: Set<String> = emptySet(),
    val selectedBikeForDetail: Bike? = null,
    val selectedBikeForOffer: Bike? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BikeRepository(application)

    val currentTab = MutableStateFlow(ScreenTab.CATALOG)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(BikeCategory.ALL)
    val selectedCategory: StateFlow<BikeCategory> = _selectedCategory.asStateFlow()

    private val _selectedBikeForDetail = MutableStateFlow<Bike?>(null)
    val selectedBikeForDetail: StateFlow<Bike?> = _selectedBikeForDetail.asStateFlow()

    private val _selectedBikeForOffer = MutableStateFlow<Bike?>(null)
    val selectedBikeForOffer: StateFlow<Bike?> = _selectedBikeForOffer.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> = repository.favoriteIds
    val recentOffers: StateFlow<List<NegotiationOffer>> = repository.recentOffers

    val allBikes: List<Bike> = repository.getAllBikes()

    val filteredBikes: StateFlow<List<Bike>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, cat ->
        repository.filterBikes(query, cat)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getAllBikes())

    val favoriteBikes: StateFlow<List<Bike>> = repository.favoriteIds
        .map { favIds: Set<String> ->
            repository.getAllBikes().filter { favIds.contains(it.id) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: ScreenTab) {
        currentTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: BikeCategory) {
        _selectedCategory.value = category
    }

    fun openBikeDetail(bike: Bike) {
        _selectedBikeForDetail.value = bike
    }

    fun closeBikeDetail() {
        _selectedBikeForDetail.value = null
    }

    fun openOfferDialog(bike: Bike) {
        _selectedBikeForOffer.value = bike
    }

    fun closeOfferDialog() {
        _selectedBikeForOffer.value = null
    }

    fun toggleFavorite(bike: Bike) {
        viewModelScope.launch {
            repository.toggleFavorite(bike.id)
            val isNowFav = repository.isFavorite(bike.id)
            _userFeedbackMessage.value = if (isNowFav) {
                "Added ${bike.name} to Saved Bikes"
            } else {
                "Removed ${bike.name} from Saved Bikes"
            }
        }
    }

    fun clearFeedbackMessage() {
        _userFeedbackMessage.value = null
    }

    fun recordSubmittedOffer(offer: NegotiationOffer) {
        repository.recordOffer(offer)
    }
}
