package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem

sealed class StoreUiState {
    object Uninitialized: StoreUiState()

    object Loading: StoreUiState()

    data class SuccessGetStores(
        val stores: List<StoreDataEntityItem>
    ): StoreUiState()

    object Error: StoreUiState()
}
