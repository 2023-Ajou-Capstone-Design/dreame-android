package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): LoginRepository {

    override suspend fun checkDuplicateNickname(nickname: String): Boolean {
        return false
    }
}