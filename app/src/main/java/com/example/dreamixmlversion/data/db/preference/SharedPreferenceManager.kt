package com.example.dreamixmlversion.data.db.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.dreamixmlversion.R
//import com.example.dreamixmlversion.di.ChildCardNumberSharedPreferences
//import com.example.dreamixmlversion.di.EmailAddressSharedPreferences
//import com.example.dreamixmlversion.di.NicknameSharedPreferences
//import com.example.dreamixmlversion.di.TownAddressSharedPreferences
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferenceManager {

    override fun putDreameEmailAddress(address: String) {
        with(sharedPreferences.edit()) {
            putString(R.string.shared_preference_dreame_email_address.toString(), address)
            apply()
        }
    }

    override fun getDreameEmailAddress(): String? {

        val value = sharedPreferences.getString(
            R.string.shared_preference_dreame_email_address.toString(),
            INVALID_STRING_VALUE
        )

        return if (value == INVALID_STRING_VALUE) {
            null
        } else {
            value
        }
    }

    override fun putDreameChildCardNumber(number: String) {
        with(sharedPreferences.edit()) {
            putString(R.string.shared_preference_dreame_child_card_number.toString(), number)
            apply()
        }
    }

    override fun getDreameChildCardNumber(): String? {

        val value = sharedPreferences.getString(
            R.string.shared_preference_dreame_child_card_number.toString(),
            INVALID_STRING_VALUE
        )

        return if (value == INVALID_STRING_VALUE) {
            null
        } else {
            value
        }
    }

    override fun putDreameTownAddress(address: String) {
        with(sharedPreferences.edit()) {
            putString(R.string.shared_preference_dreame_town_address.toString(), address)
            apply()
        }
    }

    override fun getDreameTownAddress(): String? {

        val value = sharedPreferences.getString(
            R.string.shared_preference_dreame_town_address.toString(),
            INVALID_STRING_VALUE
        )

        return if (value == INVALID_STRING_VALUE) {
            null
        } else {
            value
        }
    }

    override fun putDreameNickname(nickname: String) {
        with(sharedPreferences.edit()) {
            putString(R.string.shared_preference_dreame_nickname.toString(), nickname)
            apply()
        }
    }

    override fun getDreameNickname(): String? {

        val value = sharedPreferences.getString(
            R.string.shared_preference_dreame_nickname.toString(),
            INVALID_STRING_VALUE
        )

        return if (value == INVALID_STRING_VALUE) {
            null
        } else {
            value
        }
    }

    companion object {
        const val INVALID_STRING_VALUE = "INVALID_STRING_VALUE"
    }
}