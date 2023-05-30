package com.example.dreamixmlversion.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.LoginRepository
import com.example.dreamixmlversion.data.repository.UserRepository
import com.example.dreamixmlversion.ui.user.UserRestPointUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _emailAddress = MutableLiveData<String>()
    private val _userType = MutableLiveData<String>()
    private val _childCardNumber = MutableLiveData<String?>()
    private val _townAddress = MutableLiveData<String>()
    private val _nickname = MutableLiveData<String>()

    fun setEmailAddress(address: String) {
        _emailAddress.value = address
    }

    fun setUserType(identity: String) {
        _userType.value = identity
    }

    fun setCardNumber(cardNumber: String?) {
        if (cardNumber.isNullOrBlank()) {
            _childCardNumber.value = null
        } else {
            _childCardNumber.value = cardNumber
        }
    }

    private val _queriedTown =
        MutableLiveData<TownUiState>(TownUiState.Uninitialized)
    val queriedTown: LiveData<TownUiState> = _queriedTown

    fun requestTownList(
        region: String,
        town_to: String? = null,
        town_si: String? = null,
        town_gungu: String? = null
    ) {
        viewModelScope.launch {
            var list = listOf<String>()
            when (region) {
                TOWN_DO -> {
                    list = loginRepository.getDo()
                }
                TOWN_SI -> {
                    list = loginRepository.getSi(town_to!!)
                }
                TOWN_GUNGU -> {
                    list = loginRepository.getGunGu(town_to!!, town_si!!)
                }
                TOWN_DONG -> {
                    list = loginRepository.getDong(town_to!!, town_si!!, town_gungu!!)
                }
            }

            _queriedTown.postValue(TownUiState.SuccessGetTownList(list))
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

    private val _queriedRegisterProfileLiveData =
        MutableLiveData<LoginUiState>(LoginUiState.Uninitialized)
    val queriedRegisterProfileLiveData: LiveData<LoginUiState> = _queriedRegisterProfileLiveData

    fun registerFinally() {
        viewModelScope.launch {
            _queriedRegisterProfileLiveData.postValue(LoginUiState.Loading)
            saveToServer()
            saveToLocalDB()
        }
    }

    private fun saveToLocalDB() {
        preferenceManager.putDreameEmailAddress(_emailAddress.value.toString())
        preferenceManager.putDreameUserType(_userType.value.toString())
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
                            emailAddress = _emailAddress.value.toString(),
                            userType = _userType.value.toString(),
                            childCardNumber = _childCardNumber.value.toString(),
                            townAddress = _townAddress.value.toString(),
                            nickname = _nickname.value.toString()
                        )
                    )
                )
            } catch (_: java.lang.Exception) {

            }
        }
    }

    companion object {
        const val TOWN_DO = "TOWN_DO"
        const val TOWN_SI = "TOWN_SI"
        const val TOWN_GUNGU = "TOWN_GUNGU"
        const val TOWN_DONG = "TOWN_DONG"
    }
}