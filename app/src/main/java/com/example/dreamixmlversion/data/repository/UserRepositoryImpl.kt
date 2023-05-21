package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): UserRepository {
    override suspend fun getRestPoint(userId: String): String {
        val a = dreameApi.getMyRestPoint(userId).body()
        val b = a.toString()
        return dreameApi.getMyRestPoint(userId).body().toString()
    }

    override suspend fun changeNickname(userId: String, nickname: String): Boolean {
        return dreameApi.changeNickname(userId, nickname).body().toString() == "sucess"
    }

    override suspend fun getProfileImage(userId: String): String {
        // dreameApi.getProfileImage(userId)
        return ""
    }
}