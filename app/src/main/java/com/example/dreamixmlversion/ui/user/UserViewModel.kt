package com.example.dreamixmlversion.ui.user

import androidx.lifecycle.ViewModel
import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
): ViewModel() {

    fun getNickname(): String {
        return preferenceManager.getDreameNickname().toString()
    }

    fun changeNickname() {

    }

    fun getUserType(): String {
        return preferenceManager.getDreameUserType().toString()
    }
}