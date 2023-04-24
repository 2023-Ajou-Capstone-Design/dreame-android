package com.example.dreamixmlversion.ui.sharing.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.repository.SharingRepository
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingDetailViewModel @Inject constructor(
    private val sharingRepository: SharingRepository
): ViewModel() {

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
}