package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.SpotApi
import com.example.dreamixmlversion.data.api.response.mapper.processToList
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotRepositoryImpl @Inject constructor(
    private val spotApi: SpotApi
): SpotRepository {

//    override val spots = mutableListOf<SpotDataEntity>()

    // spots에 저장 (SpotDataEntity 형태로 가공)
    // 정렬되어 받음
    override suspend fun refreshSpots(latLng: DreameLatLng) = withContext(Dispatchers.IO) {
        spotApi.getAllSpotsNearbyUser()
            .body()?.items
    }

    override suspend fun toggleFavoriteSpot() {
        TODO("Not yet implemented")
    }
}