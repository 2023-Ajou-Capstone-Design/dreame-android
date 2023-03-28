package com.example.dreamixmlversion.ui.map

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem

sealed class StoreUiState {
    object Uninitialized: StoreUiState()

    object Loading: StoreUiState()

    data class SuccessGetSpots(
        val stores: List<StoreDataEntityItem>
    ): StoreUiState()

    object Error: StoreUiState()
}
