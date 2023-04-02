package com.example.dreamixmlversion.ui.sharing.uiState

import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity

sealed class SharingUiState {
    object Uninitialized: SharingUiState()

    object Loading: SharingUiState()

    data class SuccessGetSharingItems(
        val sharingItems: List<SharingDataItemEntity>
    ): SharingUiState()

    object Error: SharingUiState()
}
