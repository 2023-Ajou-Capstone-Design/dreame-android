package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.model.DetailInfoItem
import com.example.dreamixmlversion.data.api.response.model.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.model.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.DreameLatLng

interface StoreRepository {

    suspend fun getStoresNearbyUserForMarking(
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataForMarking>
    // mbr(거리)

    suspend fun getStoresNearbyUserByCategoryClicked(
        path: String,
        category: String?,
        subCategory: String?,
        storeType: String?,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList>

    suspend fun getStoresBySearchingKeyword(
        keyword: String,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList>

    suspend fun getDetailStoreInfo(storeId: Int, storeType: String): DetailInfoItem?

    suspend fun getFavoriteStores(
        userId: String
    ): List<StoreDataOnBottomSheetList>

    suspend fun checkFavorite(
        storeId: Int,
        storeType: String,
        userId: String
    ): Boolean

    suspend fun unCheckFavorite(
        storeId: Int,
        storeType: String,
        userId: String
    ): Boolean
}