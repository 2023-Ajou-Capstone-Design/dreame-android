package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName

data class StoreDataForMarking(
    @SerializedName("StoreID") val storeId: Int,
    @SerializedName("StoreType") val storeType: String,
    @SerializedName("StorePoitLat") val storePointLat: Float,
    @SerializedName("StorePoitLng") val storePointLng: Float,
    @SerializedName("storeName") val storeName: String
)
