package com.example.dreamixmlversion.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private fun getUserId() = preferenceManager.getDreameEmailAddress().toString()

    fun getCardNumber(): String? {
        return preferenceManager.getDreameChildCardNumber()
    }

    fun putCardNumber(cardNumber: String) {
        preferenceManager.putDreameChildCardNumber(cardNumber)
    }

    fun getNickname(): String {
        return preferenceManager.getDreameNickname().toString()
    }

    private val _queriedRestPoint =
        MutableLiveData<UserRestPointUiState>(UserRestPointUiState.Uninitialized)
    val queriedRestPoint: LiveData<UserRestPointUiState> = _queriedRestPoint

    fun getRestPoint() {
        viewModelScope.launch {
            _queriedRestPoint.postValue(
                UserRestPointUiState.SuccessGetRestPoint(
                    userRepository.getRestPoint(
                        getUserId()
                    )
                )
            )
        }
    }

//    private val _queriedChangingNicknameLivedata =
//        MutableLiveData<UserRestPointUiState>(UserRestPointUiState.Uninitialized)
//    val queriedChangingNicknameLivedata: LiveData<UserRestPointUiState> = _queriedChangingNicknameLivedata

    fun changeNickname(newNickName: String) {
        viewModelScope.launch {
            preferenceManager.putDreameNickname(newNickName)
            userRepository.changeNickname(
                getUserId(),
                newNickName
            )
        }
    }

    fun getUserType(): String {
        return preferenceManager.getDreameUserType().toString()
    }
}