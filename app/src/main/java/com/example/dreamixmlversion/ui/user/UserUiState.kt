package com.example.dreamixmlversion.ui.user

sealed class UserRestPointUiState {

    object Uninitialized: UserRestPointUiState()

    data class SuccessGetRestPoint(
        val restPoint: String
    ): UserRestPointUiState()
}

sealed class UserNicknameUiState {

    object Uninitialized: UserNicknameUiState()

    data class SuccessChangingNickname(
        val isSuccess: Boolean
    ): UserNicknameUiState()
}
