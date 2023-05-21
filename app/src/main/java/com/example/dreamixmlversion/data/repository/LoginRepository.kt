package com.example.dreamixmlversion.data.repository


interface LoginRepository {

    suspend fun getDo(): List<String>
    suspend fun getSi(townDo: String): List<String>
    suspend fun getGunGu(townDo: String, townSi: String): List<String>
    suspend fun getDong(townDo: String, townSi: String, townGunGu: String): List<String>

    suspend fun checkDuplicateNickname(
        nickname: String
    ): Boolean

    suspend fun registerUserProfile(
        emailAddress: String,
        userType: String,
        childCardNumber: String?,
        townAddress: String,
        nickname: String
    ): Boolean
}