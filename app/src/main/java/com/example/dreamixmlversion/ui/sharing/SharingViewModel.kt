package com.example.dreamixmlversion.ui.sharing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
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

    private val _detailSharingWritingId = MutableLiveData<String>()
    val detailSharingWritingId: LiveData<String> = _detailSharingWritingId

    fun setDetailSharingWritingId(writingId: String) {
        _detailSharingWritingId.value = writingId
    }

    private val sharingList = mutableListOf<SharingDataItemEntity>()
    private val _queriedSharingItemLiveData =
        MutableLiveData<SharingUiState>(SharingUiState.Uninitialized)
    val queriedSharingItemLiveData: LiveData<SharingUiState> = _queriedSharingItemLiveData

    fun getSharingItemsNearbyUser(town: String) {
        viewModelScope.launch {
            _queriedSharingItemLiveData.postValue(SharingUiState.Loading)
            sharingRepository.getSharingListInfo(town).forEach {
                sharingList.add(it)
            }
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(sharingList)
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
        title: String,
        content: String,
//        images: List<Bitmap>
    ) {
        viewModelScope.launch {
            _registerSharingLiveData.postValue(RegisterUiState.Loading)
            _registerSharingLiveData.postValue(
                RegisterUiState.RegisterSharing(
                    sharingRepository.registerNewSharing(
                        "Tester", title, content, null, "수원시 영통구 원천동"
                    )
                )
            )
            val addedData =
                SharingDataItemEntity(
                    thumbnailImage = "",
                    title = title,
                    town = "수원시 영통구 원천동",
                    uploadTime = "",
                    userId = "Tester",
                    writingId = "방금 전"
                )
            val newList = mutableListOf<SharingDataItemEntity>()
            newList.add(addedData)
            sharingList.forEach {
                newList.add(it)
            }
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(newList)
            )
        }
    }
}