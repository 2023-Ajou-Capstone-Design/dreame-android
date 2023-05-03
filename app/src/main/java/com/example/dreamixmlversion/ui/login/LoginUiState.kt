package com.example.dreamixmlversion.ui.login

sealed class LoginUiState {

    object Uninitialized : LoginUiState()

    object Loading : LoginUiState()

    data class RegisterProfile(
        val isSuccess: Boolean
    ) : LoginUiState()

    object Error : LoginUiState()
}
