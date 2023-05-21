package com.example.dreamixmlversion.ui.sharing.uiState

import com.example.dreamixmlversion.data.api.response.model.SharingDetailInfo

sealed class SharingDetailUiState {

    object Uninitialized: SharingDetailUiState()

    object Loading: SharingDetailUiState()

    data class SuccessGetDetailInfo(
        val detailInfoItem: SharingDetailInfo?
    ): SharingDetailUiState()

    object Error: SharingDetailUiState()
}
