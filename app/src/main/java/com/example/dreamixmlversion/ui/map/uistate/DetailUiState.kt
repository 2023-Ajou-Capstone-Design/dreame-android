package com.example.dreamixmlversion.ui.map.uistate


sealed class DetailUiState {
    object Uninitialized: DetailUiState()

    object Loading: DetailUiState()

    data class SuccessGetDetailInfo(
        val detailInfoItem: DetailInfoItem
    ): DetailUiState()

    object Error: DetailUiState()
}

data class DetailInfoItem(
    val storeName: String,
    val categoryName: String,
    val workingTime: String,
    val address: String,
    val phoneNumber: String,
    val providedSubject: String,
    val providedItem: String
)