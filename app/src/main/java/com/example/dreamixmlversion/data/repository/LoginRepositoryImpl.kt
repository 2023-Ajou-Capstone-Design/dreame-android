package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
) : LoginRepository {
    override suspend fun getDo(): List<String> {
        return dreameApi.getFirstRegionNameList().body()?.items?.map { it.toString() } ?: listOf()
    }

    override suspend fun getSi(townDo: String): List<String> {
        return dreameApi.getSecondRegionNameList(townDo).body()?.items?.map { it.toString() }
            ?: listOf()
    }

    override suspend fun getGunGu(townDo: String, townSi: String): List<String> {
        return dreameApi.getThirdRegionNameList(townDo, townSi).body()?.items?.map { it.toString() }
            ?: listOf()
    }

    override suspend fun getDong(townDo: String, townSi: String, townGunGu: String): List<String> {
        return dreameApi.getFourthRegionNameList(townDo, townSi, townGunGu)
            .body()?.items?.map { it.toString() } ?: listOf()
    }


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
            userPhoto = "", // image
            userId = emailAddress,
            childCardNumber = childCardNumber,
            userType = userType,
            town = townAddress,
            aka = nickname
        )
    }
}