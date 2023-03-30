package com.example.dreamixmlversion.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.StoreRepository
import com.example.dreamixmlversion.ui.map.uistate.CategoryUiState
import com.example.dreamixmlversion.ui.map.uistate.DetailUiState
import com.example.dreamixmlversion.ui.map.uistate.StoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _queriedStoresLiveData = MutableLiveData<StoreUiState>(StoreUiState.Uninitialized)
    val queriedStoresLiveData: LiveData<StoreUiState> = _queriedStoresLiveData

    fun getStoresNearbyUserForMarking(latLng: DreameLatLng, mbr: Int) {
        viewModelScope.launch {
//            _queriedStoresLiveData.postValue(StoreUiState.Loading)
            _queriedStoresLiveData.postValue(
                StoreUiState.SuccessGetStores(
                    storeRepository.getStoresNearbyUserForMarking(
                        latLng,
                        mbr
                    )
                )
            )
        }
    }

    private val _queriedStoresByClickedCategoryLiveData =
        MutableLiveData<CategoryUiState>(CategoryUiState.Uninitialized)
    val queriedStoresByClickedCategoryLiveData: LiveData<CategoryUiState> =
        _queriedStoresByClickedCategoryLiveData

    fun getStoresByClickedCategory(
        latLng: DreameLatLng,
        mbr: Int,
        storeType: String,
        category: String,
        subCategory: String
    ) {
        viewModelScope.launch {
            _queriedStoresByClickedCategoryLiveData.postValue(CategoryUiState.Loading)
            _queriedStoresByClickedCategoryLiveData.postValue(
                CategoryUiState.SuccessGetStoresByCategory(
                    storeRepository.getStoresNearbyUserByCategoryClicked(
                        storeType, category, subCategory, latLng, mbr
                    )
                )
            )
        }
    }

    private val _queriedDetailInfoLiveData =
        MutableLiveData<DetailUiState>(DetailUiState.Uninitialized)
    val queriedDetailInfoLiveData: LiveData<DetailUiState> = _queriedDetailInfoLiveData
    fun getDetailStoreInfo(storeId: Int) {
        viewModelScope.launch {
            _queriedDetailInfoLiveData.postValue(DetailUiState.Loading)
            _queriedDetailInfoLiveData.postValue(
                DetailUiState.SuccessGetDetailInfo(
                    storeRepository.getDetailStoreInfo(storeId)
                )
            )
        }
    }

//    fun toggleSpotFavorite(spot: SpotDataEntity) {
//        spotRepository.
//    }
}