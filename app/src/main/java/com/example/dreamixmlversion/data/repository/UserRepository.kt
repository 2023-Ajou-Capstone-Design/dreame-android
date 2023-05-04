package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList

interface UserRepository {

    suspend fun changeNickname(
        userId: String,
        nickname: String
    ): Boolean
}