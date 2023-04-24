package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.data.api.response.entity.DetailInfoItem


sealed class DetailUiState {
    object Uninitialized: DetailUiState()

    object Loading: DetailUiState()

    data class SuccessGetDetailInfo(
        val detailInfoItem: DetailInfoItem?
    ): DetailUiState()

    object Error: DetailUiState()
}