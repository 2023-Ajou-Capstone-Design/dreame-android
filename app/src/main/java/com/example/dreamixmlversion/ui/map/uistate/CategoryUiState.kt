package com.example.dreamixmlversion.ui.map.uistate

import com.example.dreamixmlversion.ui.map.CategoryItem

sealed class CategoryUiState {
    object Uninitialized: CategoryUiState()

//    object Loading: CategoryUiState()

    data class SuccessGetCategories(
        val categories: List<CategoryItem>
    ): CategoryUiState()

    object Error: CategoryUiState()
}

