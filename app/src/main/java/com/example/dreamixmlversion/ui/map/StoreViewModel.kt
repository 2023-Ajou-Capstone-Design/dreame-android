package com.example.dreamixmlversion.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _queriedStoresLiveData = MutableLiveData<StoreUiState>(StoreUiState.Uninitialized)
    val queriedStoresLiveData: LiveData<StoreUiState> = _queriedStoresLiveData

    fun getSpots(latLng: DreameLatLng) {
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
    val queriedCategoriesLiveData: LiveData<StoreUiState> = _queriedStoresLiveData
    fun getCategories() {
        viewModelScope.launch {
            _queriedCategoriesLiveData.postValue(CategoryUiState.SuccessGetCategories(

            ))
        }
    }

//    fun toggleSpotFavorite(spot: SpotDataEntity) {
//        spotRepository.
//    }
}