package com.example.dreamixmlversion.ui.sharing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.SharingRepository
import com.example.dreamixmlversion.ui.sharing.register.SharingImageItem
import com.example.dreamixmlversion.ui.sharing.uiState.RegisterUiState
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingViewModel @Inject constructor(
    private val sharingRepository: SharingRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    var detailSharingUserId = ""
    var detailSharingWritingId = ""

    fun setDetailSharingInfo(userId: String, writerUserId: String) {
        detailSharingUserId = userId
        detailSharingWritingId = writerUserId
    }

    private val sharingList = mutableListOf<SharingDataItemEntity>()
    private val _queriedSharingItemLiveData =
        MutableLiveData<SharingUiState>(SharingUiState.Uninitialized)
    val queriedSharingItemLiveData: LiveData<SharingUiState> = _queriedSharingItemLiveData

    private var sharingType: String? = null

    fun setSharingType(sharingType: String) {
        this.sharingType = sharingType
    }

    fun initSharingListLivedata() {
        removeAllElement()
    }

    fun getSharingItems() {
        viewModelScope.launch {
            _queriedSharingItemLiveData.postValue(SharingUiState.Loading)


            if (sharingType == SHARING_TYPE_BY_TOWN) {
                getSharingByUserPos()
            } else if (sharingType == SHARING_TYPE_BY_MY) {
                getSharingOnlyUser()
            }
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(sharingList.toMutableList())
            )
        }
    }

    private suspend fun getSharingByUserPos() {
        sharingRepository.getSharingListInfoByTown(
            preferenceManager.getDreameTownAddress().toString()
        ).forEach {
            sharingList.add(it)
        }
    }

    private suspend fun getSharingOnlyUser() {
        sharingRepository.getMySharingListInfo(
            preferenceManager.getDreameEmailAddress().toString()
        ).forEach {
            sharingList.add(it)
        }
    }

    private fun removeAllElement() {
        sharingList.removeAll(sharingList)
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
                    sharingRepository.getDetailSharingInfo(userId, writingId)
                )
            )
        }
    }

    fun setDetailUninitialized() {
        _queriedSharingDetailInfoLiveData.postValue(SharingDetailUiState.Uninitialized)
    }

    private val _registerSharingLiveData =
        MutableLiveData<RegisterUiState>(RegisterUiState.Uninitialized)
    val registerSharingLiveData: LiveData<RegisterUiState> =
        _registerSharingLiveData

    fun registerNewSharing(
        title: String,
        content: String,
        images: List<SharingImageItem>
    ) {
        viewModelScope.launch {
//            _registerSharingLiveData.postValue(RegisterUiState.Loading)
            val userId = preferenceManager.getDreameEmailAddress().toString()
//            _registerSharingLiveData.postValue(
            RegisterUiState.RegisterSharing(
                sharingRepository.registerNewSharing(
                    userId,
                    title,
                    content,
                    images.map { it.bitmapImage },
                    preferenceManager.getDreameTownAddress().toString()
                )
//                )
            )
            val addedData =
                SharingDataItemEntity(
                    thumbnailImage = "",
                    title = title,
                    town = preferenceManager.getDreameTownAddress().toString(),
                    uploadTime = "방금 전",
                    userId = userId,
                    writingId = ""
                )
//            val newList = mutableListOf<SharingDataItemEntity>()
//            newList.add(addedData)
            sharingList.add(0, addedData)
            _queriedSharingItemLiveData.postValue(
                SharingUiState.SuccessGetSharingItems(sharingList.toMutableList())
            )
        }
    }

    fun isSameWriter(sharingUserId: String): Boolean {
        val userId = preferenceManager.getDreameEmailAddress().toString()
        return userId == sharingUserId
    }

    var modifyMode = false

    fun modifySharing(
        title: String,
        content: String,
        images: List<SharingImageItem>,
        writingId: String
    ) {
        viewModelScope.launch {
            sharingRepository.modifySharing(
                preferenceManager.getDreameEmailAddress().toString(),
                title,
                content,
                images.map { it.bitmapImage },
                writingId,
                preferenceManager.getDreameTownAddress().toString()
            )
        }
    }

    fun deleteSharing() {
        var deletingItem: SharingDataItemEntity? = null
        sharingList.forEach {
            if (it.writingId == detailSharingWritingId) {
                deletingItem = it
            }
        }

        sharingList.remove(deletingItem)

        _queriedSharingItemLiveData.postValue(
            SharingUiState.SuccessGetSharingItems(sharingList.toMutableList())
        )

        viewModelScope.launch {

            sharingRepository.deleteSharing(
                preferenceManager.getDreameEmailAddress().toString(),
                detailSharingWritingId
            )
        }
    }

    fun stopUsingListViewModel() {

    }

    companion object {
        const val SHARING_TYPE_BY_TOWN = "SHARING_TYPE_BY_TOWN"
        const val SHARING_TYPE_BY_MY = "SHARING_TYPE_BY_MY"
    }
}