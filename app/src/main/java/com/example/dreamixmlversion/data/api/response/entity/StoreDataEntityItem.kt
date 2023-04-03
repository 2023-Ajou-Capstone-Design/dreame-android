package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName

data class StoreDataForMarking(
    @SerializedName("StoreID") val storeId: Int,
    @SerializedName("StoreType") val storeType: String,
    @SerializedName("StorePointLat") val storePointLat: Float,
    @SerializedName("StorePointLng") val storePointLng: Float,
    @SerializedName("StoreName") val storeName: String
)

data class StoreDataOnBottomSheetList(
    @SerializedName("StoreID") val storeId: Int,
    // 01: 선한영향력 가게, 02: 아동급식카드가맹점, 03: 둘다, 04: 기타
    @SerializedName("StoreType") val storeType: String,
    @SerializedName("StorePointLat") val storePointLat: Float,
    @SerializedName("StorePointLng") val storePointLng: Float,
    @SerializedName("StoreName") val storeName: String,
    // 01: 식음료, 02: 마트, 04: 교육, 05: 생활, 99: 기타
    @SerializedName("Category") val category: String,
    @SerializedName("SubCategory") val subCategory: String,
    @SerializedName("CateName") val cateName: String,
    @SerializedName("SubCateName") val subCateName: String,
    // Base 64 Encoding/Decoding
    @SerializedName("StorePhoto") val storePhoto: String
)

//data class StoreDataBySearchingKeyword(
//    @SerializedName("StoreID") val storeId: Int,
//    @SerializedName("StoreType") val storeType: String,
//    @SerializedName("StorePhoto") val storePhoto: String,
//    @SerializedName("StoreName") val storeName: String,
//    @SerializedName("CateName") val cateName: String,
//    @SerializedName("SubCateName") val subCateName: String,
//    @SerializedName("StorePointLng") val storePointLng: Float,
//    @SerializedName("StorePointLat") val storePointLat: Float,
//    @SerializedName("Category") val category: String,
//    @SerializedName("SubCategory") val subCategory: String
//)