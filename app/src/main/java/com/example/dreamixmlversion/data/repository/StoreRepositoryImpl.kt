package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameMapApi
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.CategoryItem
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dreameApi: DreameMapApi
): StoreRepository {

//    override val spots = mutableListOf<SpotDataEntity>()

    // spots에 저장 (SpotDataEntity 형태로 가공)
    // 정렬되어 받음
    override suspend fun getStoresNearbyUserForMarking(
        latLng: DreameLatLng,
        mbr: Int
    ) = withContext(Dispatchers.IO) {
//        dreameApi.getAllStoresNearbyUserForMarking(latLng.lat.toFloat(), latLng.lng.toFloat(), mbr)
//            .body()?.items ?: listOf()
        dreameApi.getAllStoresNearbyUserForMarking()
            .body()?.items ?: listOf()
    }

    override suspend fun getAllCategories(): List<CategoryItem> {
        return dreameApi.getAllCategories()
    }

    override suspend fun getDetailStoreInfo(storeId: Int): DetailInfoItem {

        // todo : storeId 파라미터로 api 호출
//        dreameApi.getDetailSpotData()

        return DetailInfoItem(
            "사나운치킨", "치킨", "16:00~03:00",
            "수원시 영통구 원천동 1-1", "031-123-3467",
            "결식아동, 소방관", "닭"
        )
    }

    override suspend fun toggleFavoriteSpot() {
        TODO("Not yet implemented")
    }
}