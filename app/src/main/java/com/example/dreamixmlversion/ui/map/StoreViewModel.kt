package com.example.dreamixmlversion.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.StoreRepository
import com.example.dreamixmlversion.ui.map.uistate.BottomSheetListUiState
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
                        latLng, mbr
                    )
                )
            )
        }
    }

    private val _queriedStoresOnBottomSheetListLiveData =
        MutableLiveData<BottomSheetListUiState>(BottomSheetListUiState.Uninitialized)
    val queriedStoresOnBottomSheetListLiveData: LiveData<BottomSheetListUiState> =
        _queriedStoresOnBottomSheetListLiveData

    fun getStoresByClickedCategory(
        path: String, latLng: DreameLatLng, mbr: Int, storeType: String?, category: String?, subCategory: String?
    ) {
        viewModelScope.launch {
            _queriedStoresOnBottomSheetListLiveData.postValue(BottomSheetListUiState.Loading)
            _queriedStoresOnBottomSheetListLiveData.postValue(
                BottomSheetListUiState.SuccessGetStoresOnBottomSheetList(
                    storeRepository.getStoresNearbyUserByCategoryClicked(
                        path = path,
                        category = category,
                        subCategory = subCategory,
                        storeType = storeType,
                        latLng = latLng,
                        mbr = mbr
                    )
                )
            )
        }
    }

    fun getStoresBySearchingKeyword(keyword: String, latLng: DreameLatLng, mbr: Int) {
        viewModelScope.launch {
            _queriedStoresOnBottomSheetListLiveData.postValue(BottomSheetListUiState.Loading)
            _queriedStoresOnBottomSheetListLiveData.postValue(
                BottomSheetListUiState.SuccessGetStoresOnBottomSheetList(
                    storeRepository.getStoresBySearchingKeyword(
                        keyword, latLng, mbr
                    )
                )
            )
        }
    }

    private val _queriedDetailInfoLiveData =
        MutableLiveData<DetailUiState>(DetailUiState.Uninitialized)
    val queriedDetailInfoLiveData: LiveData<DetailUiState> = _queriedDetailInfoLiveData
    fun getDetailStoreInfo(storeId: Int, storeType: String) {
        viewModelScope.launch {
            _queriedDetailInfoLiveData.postValue(DetailUiState.Loading)
            _queriedDetailInfoLiveData.postValue(
                DetailUiState.SuccessGetDetailInfo(
                    storeRepository.getDetailStoreInfo(storeId, storeType)
                )
            )
        }
    }

    fun getFavoriteStores(userId: String) {
        viewModelScope.launch {
            _queriedStoresOnBottomSheetListLiveData.postValue(BottomSheetListUiState.Loading)
            _queriedStoresOnBottomSheetListLiveData.postValue(
                BottomSheetListUiState.SuccessGetStoresOnBottomSheetList(
                    storeRepository.getFavoriteStores(userId)
                )
            )
        }
    }

//    fun updateFavorite(storeId: Int, storeType: String, userId: String) {
//        viewModelScope.launch {
//            storeRepository.
//        }
//    }
}