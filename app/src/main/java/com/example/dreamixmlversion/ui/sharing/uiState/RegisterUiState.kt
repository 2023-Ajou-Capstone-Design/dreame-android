package com.example.dreamixmlversion.ui.sharing.uiState

sealed class RegisterUiState {

    object Uninitialized: RegisterUiState()

    object Loading: RegisterUiState()

    data class RegisterSharing(
        val isSuccess: Boolean
    ): RegisterUiState()

    object Error: RegisterUiState()
}