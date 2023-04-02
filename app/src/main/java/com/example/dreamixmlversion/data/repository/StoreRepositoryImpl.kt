package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
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

    override suspend fun getStoresNearbyUserByCategoryClicked(
        storeType: String,
        category: String,
        subCategory: String,
        latLng: DreameLatLng,
        mbr: Int
    ) = withContext(Dispatchers.IO) {
        dreameApi.getStoresClickedCategoryName(
//            storeType, category, subCategory,
//            latLng.lat.toFloat(), latLng.lng.toFloat(), mbr
        ).body()?.items ?: listOf()
    }

    override suspend fun getStoresBySearchingKeyword(
        keyword: String,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList> = withContext(Dispatchers.IO) {
        dreameApi.searchByKeyword(
//            keyword,
//            latLng.lat.toFloat(),
//            latLng.lng.toFloat(),
//            mbr
        ).body()?.items ?: listOf()
    }


    override suspend fun getDetailStoreInfo(storeId: Int, storeType: String)= withContext(Dispatchers.IO) {

        // todo : storeId 파라미터로 api 호출
//        dreameApi.getDetailSpotData(storeId, storeType)

        DetailInfoItem(
            "사나운치킨", "치킨", "16:00~03:00",
            "수원시 영통구 원천동 1-1", "031-123-3467",
            "결식아동, 소방관", "닭"
        )
    }

    override suspend fun getFavoriteStores(userId: String) = withContext(Dispatchers.IO) {
//        dreameApi.getFavoriteStores(userId).body()?.items ?: listOf()
        dreameApi.getFavoriteStores().body()?.items ?: listOf()
    }

}