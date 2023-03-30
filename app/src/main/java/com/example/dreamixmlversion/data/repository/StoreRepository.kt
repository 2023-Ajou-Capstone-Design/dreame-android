package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem

interface StoreRepository {

    suspend fun getStoresNearbyUserForMarking(
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataForMarking>
    // mbr(거리)

    suspend fun getStoresNearbyUserByCategoryClicked(
        storeType: String,
        category: String,
        subCategory: String,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList>

    suspend fun getStoresBySearchingKeyword(
        keyword: String,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList>

    suspend fun getDetailStoreInfo(storeId: Int, storeType: String): DetailInfoItem

    suspend fun toggleFavoriteSpot()
}