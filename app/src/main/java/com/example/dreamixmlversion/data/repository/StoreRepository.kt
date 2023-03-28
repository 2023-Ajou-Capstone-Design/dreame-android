package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem
import com.example.dreamixmlversion.data.db.entity.DreameLatLng

interface StoreRepository {

    suspend fun refreshSpots(latLng: DreameLatLng): List<StoreDataEntityItem>?

    suspend fun toggleFavoriteSpot()
}