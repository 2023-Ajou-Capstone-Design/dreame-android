package com.example.dreamixmlversion.data.api.response.model.login

import com.google.gson.annotations.SerializedName

data class DoResponse(
    @SerializedName("items") val items: List<TownDo>,
    @SerializedName("total") val total: Int
)

data class TownDo(
    @SerializedName("D") val d: String
)

data class SiResponse(
    @SerializedName("items") val items: List<TownSi>,
    @SerializedName("total") val total: Int
)

data class TownSi(
    @SerializedName("S") val s: String
)

data class GunGuResponse(
    @SerializedName("items") val items: List<TownGunGu>,
    @SerializedName("total") val total: Int
)

data class TownGunGu(
    @SerializedName("G") val g: String
)

data class DongResponse(
    @SerializedName("items") val items: List<TownDong>,
    @SerializedName("total") val total: Int
)

data class TownDong(
    @SerializedName("D") val d: String
)