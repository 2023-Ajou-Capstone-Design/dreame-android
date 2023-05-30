package com.example.dreamixmlversion.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import com.example.dreamixmlversion.data.repository.LoginRepository
import com.example.dreamixmlversion.data.repository.UserRepository
import com.example.dreamixmlversion.ui.login.LoginViewModel
import com.example.dreamixmlversion.ui.login.TownUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TownViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
): ViewModel() {

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
                LoginViewModel.TOWN_DO -> {
                    list = loginRepository.getDo()
                }
                LoginViewModel.TOWN_SI -> {
                    list = loginRepository.getSi(town_to!!)
                }
                LoginViewModel.TOWN_GUNGU -> {
                    list = loginRepository.getGunGu(town_to!!, town_si!!)
                }
                LoginViewModel.TOWN_DONG -> {
                    list = loginRepository.getDong(town_to!!, town_si!!, town_gungu!!)
                }
            }

            _queriedTown.postValue(TownUiState.SuccessGetTownList(list))
        }
    }

    fun updateTown(address: String) {
        viewModelScope.launch {
            preferenceManager.putDreameTownAddress(address)
            userRepository.updateNewTown(preferenceManager.getDreameEmailAddress().toString(), address)
        }
    }
}