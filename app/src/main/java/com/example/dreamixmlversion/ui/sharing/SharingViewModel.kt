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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharingViewModel @Inject constructor(
    private val sharingRepository: SharingRepository
) : ViewModel() {


    private val _queriedSharingItemLiveData =
        MutableLiveData<SharingUiState>(SharingUiState.Uninitialized)
    val queriedSharingItemLiveData: LiveData<SharingUiState> = _queriedSharingItemLiveData

    fun getSharingItemsNearbyUser(town: String) {
        viewModelScope.launch {
//            _queriedStoresLiveData.postValue(StoreUiState.Loading)
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(
                    sharingRepository.getSharingListInfo(
                        town
                    )
                )
            )
        }
    }
}