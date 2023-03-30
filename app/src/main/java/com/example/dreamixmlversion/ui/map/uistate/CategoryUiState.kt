package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.data.api.response.entity.StoreDataByCategoryClicked


sealed class CategoryUiState {
    object Uninitialized: CategoryUiState()

    object Loading: CategoryUiState()

    data class SuccessGetStoresByCategory(
        val stores: List<StoreDataByCategoryClicked>
    ): CategoryUiState()

    object Error: CategoryUiState()
}

