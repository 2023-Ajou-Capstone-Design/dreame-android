package com.example.dreamixmlversion.ui.sharing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.data.repository.SharingRepository
import com.example.dreamixmlversion.ui.map.uistate.DetailUiState
import com.example.dreamixmlversion.ui.map.uistate.StoreUiState
import com.example.dreamixmlversion.ui.sharing.uiState.RegisterUiState
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharingViewModel @Inject constructor(
    private val sharingRepository: SharingRepository
) : ViewModel() {

    private val _queriedSharingItemLiveData =
        MutableLiveData<SharingUiState>(SharingUiState.Uninitialized)
    val queriedSharingItemLiveData: LiveData<SharingUiState> = _queriedSharingItemLiveData

    fun getSharingItemsNearbyUser(latLng: DreameLatLng, mbr: Int) {
        viewModelScope.launch {
//            _queriedStoresLiveData.postValue(StoreUiState.Loading)
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(
                    sharingRepository.getSharingListInfo(
                        latLng, mbr
                    )
                )
            )
        }
    }

    private val _queriedSharingDetailInfoLiveData =
        MutableLiveData<SharingDetailUiState>(SharingDetailUiState.Uninitialized)
    val queriedSharingDetailInfoLiveData: LiveData<SharingDetailUiState> =
        _queriedSharingDetailInfoLiveData
    fun getSharingDetailInfo(userId: String, writingId: String) {
        viewModelScope.launch {
            _queriedSharingDetailInfoLiveData.postValue(SharingDetailUiState.Loading)
            _queriedSharingDetailInfoLiveData.postValue(
                SharingDetailUiState.SuccessGetDetailInfo(
                    sharingRepository.getDetailSharingInfo(
                        userId, writingId
                    )
                )
            )
        }
    }


    private val _registerSharingLiveData =
        MutableLiveData<RegisterUiState>(RegisterUiState.Uninitialized)
    val registerSharingLiveData: LiveData<RegisterUiState> =
        _registerSharingLiveData


    fun registerNewSharing(
        userId: String,
        writingId: String,
        title: String,
        content: String,
        photo1: String,
        photo2: String,
        photo3: String
    ) {
        viewModelScope.launch {
            _registerSharingLiveData.postValue(RegisterUiState.Loading)
            _registerSharingLiveData.postValue(
                RegisterUiState.RegisterSharing(
                    sharingRepository.registerNewSharing(
                        userId, writingId, title, content, photo1, photo2, photo3
                    )
                )
            )
        }
    }
}