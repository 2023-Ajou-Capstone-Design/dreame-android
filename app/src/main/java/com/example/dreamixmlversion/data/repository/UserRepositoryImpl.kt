package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): UserRepository {

    override suspend fun changeNickname(userId: String, nickname: String): Boolean {
        return dreameApi.changeNickname(userId, nickname)
    }
}