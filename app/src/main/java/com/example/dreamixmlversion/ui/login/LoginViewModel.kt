package com.example.dreamixmlversion.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.LoginRepository
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _emailAddress = MutableLiveData<String>()
    private val _identity = MutableLiveData<String>()
    private val _childCardNumber = MutableLiveData<String?>()
    private val _townAddress = MutableLiveData<String>()
    private val _nickname = MutableLiveData<String>()

    fun setEmailAddress(address: String) {
        _emailAddress.value = address
    }

    fun setIdentity(identity: String) {
        _identity.value = identity
    }

    fun setCardNumber(cardNumber: String?) {
        if (cardNumber.isNullOrBlank()) {
            _childCardNumber.value = null
        } else {
            _childCardNumber.value = cardNumber
        }
    }

    fun setTownAddress(address: String) {
        _townAddress.value = address
    }

    fun isDuplicateNickname(nickname: String): Boolean {
        var isDuplicate = false
        viewModelScope.launch {
            if (loginRepository.checkDuplicateNickname(nickname)) {
                isDuplicate = true
            }
        }
        return isDuplicate
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun registerFinally() {
        saveToServer()

        saveToLocalDB()
    }

    private val _queriedRegisterProfileLiveData =
        MutableLiveData<LoginUiState>(LoginUiState.Uninitialized)
    val queriedRegisterProfileLiveData: LiveData<LoginUiState> = _queriedRegisterProfileLiveData

    private fun saveToLocalDB() {
        preferenceManager.putDreameEmailAddress(_emailAddress.value.toString())
        preferenceManager.putDreameChildCardNumber(_childCardNumber.value.toString())
        preferenceManager.putDreameTownAddress(_townAddress.value.toString())
        preferenceManager.putDreameNickname(_nickname.value.toString())
    }

    private fun saveToServer() {
        viewModelScope.launch {
            try {
                _queriedRegisterProfileLiveData.postValue(
                    LoginUiState.RegisterProfile(
                        loginRepository.registerUserProfile(
                            _emailAddress.value.toString(),
                            _identity.value.toString(),
                            _childCardNumber.value.toString(),
                            _townAddress.value.toString(),
                            _nickname.value.toString()
                        )
                    )
                )
            } catch (_: java.lang.Exception) {

            }
        }
    }
}