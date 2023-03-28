package com.example.dreamixmlversion.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.SpotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val spotRepository: SpotRepository
) : ViewModel() {

    private val _queriedSpotsLiveData = MutableLiveData<StoreUiState>(StoreUiState.Uninitialized)
    val queriedSpotsLiveData: LiveData<StoreUiState> = _queriedSpotsLiveData

    fun getSpots(latLng: DreameLatLng) {
        viewModelScope.launch {
            _queriedSpotsLiveData.postValue(StoreUiState.Loading)
            _queriedSpotsLiveData.postValue(
                StoreUiState.SuccessGetSpots(
                    spotRepository.refreshSpots(
                        latLng
                    ) ?: listOf()
                )
            )
        }
    }

//    fun toggleSpotFavorite(spot: SpotDataEntity) {
//        spotRepository.
//    }
}