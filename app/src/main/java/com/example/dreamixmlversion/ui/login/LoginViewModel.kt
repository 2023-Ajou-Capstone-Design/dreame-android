package com.example.dreamixmlversion.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _emailAddress = MutableLiveData<String>()
    val emailAddress: LiveData<String> = _emailAddress

    private val _child = MutableLiveData<Map<Boolean, String?>>()
    val child: LiveData<Map<Boolean, String?>> = _child

    private val _townAddress = MutableLiveData<String>()
    val townAddress: LiveData<String> = _townAddress

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    fun setEmailAddress(address: String) {
        _emailAddress.value = address
    }

    fun setCardNumber(cardNumber: String?) {
        if (cardNumber.isNullOrBlank()) {
            _child.value = mapOf(false to null)
        } else {
            _child.value = mapOf(true to cardNumber)
        }
    }

    fun setTownAddress(address: String) {
        _townAddress.value = address
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }
}