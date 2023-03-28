package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): StoreRepository {

//    override val spots = mutableListOf<SpotDataEntity>()

    // spots에 저장 (SpotDataEntity 형태로 가공)
    // 정렬되어 받음
    override suspend fun refreshSpots(latLng: DreameLatLng) = withContext(Dispatchers.IO) {
        dreameApi.getAllSpotsNearbyUser()
            .body()?.items
    }

    override suspend fun toggleFavoriteSpot() {
        TODO("Not yet implemented")
    }
}