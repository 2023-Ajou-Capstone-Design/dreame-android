package com.example.dreamixmlversion

import androidx.lifecycle.ViewModel
import com.example.dreamixmlversion.data.db.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
): ViewModel() {

    fun getEmailAddress() =
        preferenceManager.getDreameEmailAddress()
}