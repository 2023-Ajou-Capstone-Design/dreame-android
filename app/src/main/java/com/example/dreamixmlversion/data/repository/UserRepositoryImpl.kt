package com.example.dreamixmlversion.data.repository

import android.util.Log
import com.example.dreamixmlversion.data.api.DreameApi
import com.google.gson.Gson
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): UserRepository {
    override suspend fun getRestPoint(userId: String): String {
        Log.d("restPoint", dreameApi.getMyRestPoint(userId).body().toString())
        return dreameApi.getMyRestPoint(userId).body().toString()
    }

    override suspend fun changeNickname(userId: String, nickname: String): Boolean {
        return dreameApi.changeNickname(userId, nickname).body() == "sucess"
    }

    override suspend fun getProfileImage(userId: String): String {
        // dreameApi.getProfileImage(userId)
        return ""
    }

    override suspend fun updateNewTown(userId: String, address: String): String {
        return dreameApi.updateTown(userId, address).body().toString()
    }
}