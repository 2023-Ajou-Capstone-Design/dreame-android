package com.example.dreamixmlversion.data.api.response.entity


import com.google.gson.annotations.SerializedName

data class StoreDataEntityItem(
    @SerializedName("Category")
    val category: Int?,
    @SerializedName("StoreID")
    val storeID: Int?,
    @SerializedName("StorePoitLat")
    val storePoitLat: Double?,
    @SerializedName("StorePoitLng")
    val storePoitLng: Double?,
    @SerializedName("StoreType")
    val storeType: Int?,
    @SerializedName("SubCategory")
    val subCategory: Int?
)