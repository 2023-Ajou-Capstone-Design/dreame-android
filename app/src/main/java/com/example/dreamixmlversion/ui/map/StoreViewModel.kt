package com.example.dreamixmlversion.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.CategoryRepository
import com.example.dreamixmlversion.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _queriedStoresLiveData = MutableLiveData<StoreUiState>(StoreUiState.Uninitialized)
    val queriedStoresLiveData: LiveData<StoreUiState> = _queriedStoresLiveData

    fun getStores(latLng: DreameLatLng) {
        viewModelScope.launch {
            _queriedStoresLiveData.postValue(StoreUiState.Loading)
            _queriedStoresLiveData.postValue(
                StoreUiState.SuccessGetStores(
                    storeRepository.refreshSpots(
                        latLng
                    ) ?: listOf()
                )
            )
        }
    }

    private val _queriedCategoriesLiveData = MutableLiveData<CategoryUiState>(CategoryUiState.Uninitialized)
    val queriedCategoriesLiveData: LiveData<CategoryUiState> = _queriedCategoriesLiveData
    fun getCategories() {
        viewModelScope.launch {
            _queriedCategoriesLiveData.postValue(
                CategoryUiState.SuccessGetCategories(
                    categoryRepository.getAllCategories()
                )
            )
        }
    }

//    fun toggleSpotFavorite(spot: SpotDataEntity) {
//        spotRepository.
//    }
}