package com.example.dreamixmlversion.data.db.preference

interface PreferenceManager {

    fun putDreameEmailAddress(address: String)

    fun getDreameEmailAddress(): String?

    fun putDreameUserType(userType: String)

    fun getDreameUserType(): String?

    fun putDreameChildCardNumber(number: String)

    fun getDreameChildCardNumber(): String?

    fun putDreameTownAddress(address: String)

    fun getDreameTownAddress(): String?

    fun putDreameNickname(nickname: String)

    fun getDreameNickname(): String?
}