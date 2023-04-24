package com.example.dreamixmlversion.ui.sharing.register

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.repository.SharingRepository
import com.example.dreamixmlversion.ui.sharing.uiState.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val sharingRepository: SharingRepository
): ViewModel() {
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
        }
    }
}