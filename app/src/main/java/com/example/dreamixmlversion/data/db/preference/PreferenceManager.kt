package com.example.dreamixmlversion.data.db.preference

import android.provider.ContactsContract.CommonDataKinds.Nickname

interface PreferenceManager {

    fun putDreameEmailAddress(address: String)

    fun getDreameEmailAddress(): String?

    fun putDreameChildCardNumber(number: String)

    fun getDreameChildCardNumber(): String?

    fun putDreameTownAddress(address: String)

    fun getDreameTownAddress(): String?

    fun putDreameNickname(nickname: String)

    fun getDreameNickname(): String?
}