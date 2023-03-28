package com.example.dreamixmlversion.ui.map

sealed class CategoryUiState {
    object Uninitialized: CategoryUiState()

//    object Loading: CategoryUiState()

    data class SuccessGetCategories(
        val categories: List<CategoryItem>
    ): CategoryUiState()

    object Error: CategoryUiState()
}

