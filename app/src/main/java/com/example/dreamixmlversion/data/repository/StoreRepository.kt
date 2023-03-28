package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem

interface StoreRepository {

    suspend fun refreshSpots(latLng: DreameLatLng): List<StoreDataEntityItem>?

    suspend fun getDetailStoreInfo(storeId: Int): DetailInfoItem

    suspend fun toggleFavoriteSpot()
}