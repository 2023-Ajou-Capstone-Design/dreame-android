package com.example.dreamixmlversion.ui.sharing.uiState

import com.example.dreamixmlversion.data.api.response.entity.SharingRegister

sealed class RegisterUiState {

    object Uninitialized: RegisterUiState()

    object Loading: RegisterUiState()

    data class RegisterSharing(
        val isSuccess: Boolean
    ): RegisterUiState()

    object Error: RegisterUiState()
}