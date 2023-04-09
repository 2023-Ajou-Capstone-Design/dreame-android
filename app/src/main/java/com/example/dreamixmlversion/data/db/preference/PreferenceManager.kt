package com.example.dreamixmlversion.data.db.preference

interface PreferenceManager {

    fun putDreameAddress(address: String)

    fun getDreameAddress(): String?
}