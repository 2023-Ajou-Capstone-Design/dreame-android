package com.example.dreamixmlversion.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _emailAddress = MutableLiveData<String>()
//    val emailAddress: LiveData<String> = _emailAddress

    //    private val _childCardNumber = MutableLiveData<Map<Boolean, String?>>()
    private val _childCardNumber = MutableLiveData<String?>()
//    val childCardNumber: LiveData<Map<Boolean, String?>> = _childCardNumber

    private val _townAddress = MutableLiveData<String>()
//    val townAddress: LiveData<String> = _townAddress

    private val _nickname = MutableLiveData<String>()
//    val nickname: LiveData<String> = _nickname

    fun setEmailAddress(address: String) {
        _emailAddress.value = address
    }

    fun setCardNumber(cardNumber: String?) {
        if (cardNumber.isNullOrBlank()) {
//            _childCardNumber.value = mapOf(false to null)
            _childCardNumber.value = null
        } else {
//            _childCardNumber.value = mapOf(true to cardNumber)
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
        preferenceManager.putDreameEmailAddress(_emailAddress.value.toString())
        preferenceManager.putDreameChildCardNumber(_childCardNumber.value.toString())
        preferenceManager.putDreameTownAddress(_townAddress.value.toString())
        preferenceManager.putDreameNickname(_nickname.value.toString())
    }

    fun getRegistrationInfo(): String {
        return "email: ${preferenceManager.getDreameEmailAddress()}\n" +
                "cardNumber: ${preferenceManager.getDreameChildCardNumber()}\n" +
                "town: ${preferenceManager.getDreameTownAddress()}\n" +
                "nickname: ${preferenceManager.getDreameNickname()}"
    }
}