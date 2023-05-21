package com.example.dreamixmlversion.ui.login

import com.example.dreamixmlversion.ui.user.UserRestPointUiState

sealed class TownUiState {
    object Uninitialized: TownUiState()

    data class SuccessGetTownList(
        val townList: List<String>
    ): TownUiState()
}
