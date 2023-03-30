package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem
import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.entity.StoreMarkingResponse
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.CategoryItem
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem

interface StoreRepository {

    suspend fun getStoresNearbyUserForMarking(
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataForMarking>
    // mbr(거리)

    suspend fun getAllCategories(): List<CategoryItem>

    suspend fun getDetailStoreInfo(storeId: Int): DetailInfoItem

    suspend fun toggleFavoriteSpot()
}