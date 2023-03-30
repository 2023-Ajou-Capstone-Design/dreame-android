package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList


sealed class BottomSheetListUiState {
    object Uninitialized: BottomSheetListUiState()

    object Loading: BottomSheetListUiState()

    data class SuccessGetStoresOnBottomSheetList(
        val stores: List<StoreDataOnBottomSheetList>
    ): BottomSheetListUiState()

    object Error: BottomSheetListUiState()
}

