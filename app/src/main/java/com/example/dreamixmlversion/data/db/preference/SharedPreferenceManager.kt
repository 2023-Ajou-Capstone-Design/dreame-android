package com.example.dreamixmlversion.data.db.preference

import android.content.SharedPreferences
import com.example.dreamixmlversion.R
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    private val sharedPreference: SharedPreferences
) : PreferenceManager {

    override fun putDreameAddress(address: String) {
        with(sharedPreference.edit()) {
            putString(address, INVALID_STRING_VALUE)
            apply()
        }
    }

    override fun getDreameAddress(): String? {
        return sharedPreference.getString(
            R.string.shared_preference_manager_key.toString(),
            INVALID_STRING_VALUE
        )
    }

    companion object {
        const val INVALID_STRING_VALUE = "INVALID_STRING_VALUE"
    }
}