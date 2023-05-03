package com.example.dreamixmlversion.data.repository

interface LoginRepository {

    suspend fun checkDuplicateNickname(
        nickname: String
    ): Boolean

    suspend fun registerUserProfile(
        emailAddress: String,
        userType: String,
        childCardNumber: String? = null,
        townAddress: String,
        nickname: String
    ): Boolean
}