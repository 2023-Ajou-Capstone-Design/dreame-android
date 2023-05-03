package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): LoginRepository {

    override suspend fun checkDuplicateNickname(nickname: String): Boolean {
        return false
    }

    override suspend fun registerUserProfile(
        emailAddress: String,
        userType: String,
        childCardNumber: String?,
        townAddress: String,
        nickname: String
    ): Boolean {
        return dreameApi.registerUserProfile(
            profile = "", // image
            account = emailAddress,
            childCardNumber = childCardNumber,
            userType = userType,
            town = townAddress,
            aka = nickname
        )
    }
}