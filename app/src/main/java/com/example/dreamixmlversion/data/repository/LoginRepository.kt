package com.example.dreamixmlversion.data.repository

interface LoginRepository {

    suspend fun checkDuplicateNickname(
        nickname: String
    ): Boolean
}