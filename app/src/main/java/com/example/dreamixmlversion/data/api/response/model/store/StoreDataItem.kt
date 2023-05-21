package com.example.dreamixmlversion.data.api.response.model

import com.google.gson.annotations.SerializedName

data class StoreDataForMarking(
    @SerializedName("Distance") val distance: Double,
    @SerializedName("StoreID") val storeId: Int,
    @SerializedName("StoreName") val storeName: String,
    @SerializedName("StorePointLat") val storePointLat: Float,
    @SerializedName("StorePointLng") val storePointLng: Float,
    @SerializedName("StoreType") val storeType: String
)

data class StoreDataOnBottomSheetList(
    @SerializedName("CateName") val cateName: String,
    @SerializedName("Category") val category: String,
    @SerializedName("Distance") val distance: Double,
    @SerializedName("StoreID") val storeId: Int,
    @SerializedName("StoreName") val storeName: String,
    @SerializedName("StorePhoto") val storePhoto: String,
    @SerializedName("StorePointLat") val storePointLat: Float,
    @SerializedName("StorePointLng") val storePointLng: Float,
    // 01: 선한영향력 가게, 02: 아동급식카드가맹점, 03: 둘다, 04: 기타
    @SerializedName("StoreType") val storeType: String,
    // 01: 식음료, 02: 마트, 04: 교육, 05: 생활, 99: 기타
    @SerializedName("SubCateName") val subCateName: String,
    @SerializedName("SubCategory") val subCategory: String
)

data class DetailInfoItem(
    @SerializedName("Address") val address: String,
    @SerializedName("CateName") val cateName: String,
    @SerializedName("DayFinish") val dayFinish: String,
    @SerializedName("DayStart") val dayStart: String,
    @SerializedName("DetailAddress") val detailAddress: String,
    @SerializedName("HoliFinish") val holiFinish: String,
    @SerializedName("HoliStart") val holiStart: String,
    @SerializedName("Item") val item: String,
    @SerializedName("Phone") val phone: String,
    @SerializedName("Provided1") val provided1: String,
    @SerializedName("Provided2") val provided2: String,
    @SerializedName("SatFinish") val satFinish: String,
    @SerializedName("SatStart") val satStart: String,
    @SerializedName("StoreID") val storeID: Int,
    @SerializedName("StoreName") val storeName: String,
    @SerializedName("StorePhoto") val storePhoto: String,
    @SerializedName("StoreType") val storeType: String,
    @SerializedName("SubCateName") val subCateName: String,
    @SerializedName("WorkDay") val workDay: String,
    @SerializedName("Bookmarked") val isBookmark: Int
)