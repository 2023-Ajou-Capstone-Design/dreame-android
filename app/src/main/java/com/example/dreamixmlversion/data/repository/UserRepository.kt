package com.example.dreamixmlversion.data.repository

interface UserRepository {

    suspend fun getRestPoint(userId: String): String

    suspend fun changeNickname(
        userId: String,
        nickname: String
    ): Boolean

    suspend fun getProfileImage(userId: String): String

    suspend fun updateNewTown(userId: String, address: String): String
}