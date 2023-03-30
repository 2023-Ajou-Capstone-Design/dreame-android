package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking

sealed class StoreUiState {
    object Uninitialized: StoreUiState()

    data class SuccessGetStores(
        val stores: List<StoreDataForMarking>
    ): StoreUiState()

    object Error: StoreUiState()
}
