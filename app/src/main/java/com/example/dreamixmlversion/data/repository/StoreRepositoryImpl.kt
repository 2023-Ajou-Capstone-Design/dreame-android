package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): StoreRepository {

    // spots에 저장 (SpotDataEntity 형태로 가공)
    // 정렬되어 받음
    override suspend fun getStoresNearbyUserForMarking(
        latLng: DreameLatLng,
        mbr: Int
    ) = withContext(Dispatchers.IO) {
        dreameApi.getAllStoresNearbyUserForMarking(latLng.lat.toFloat(), latLng.lng.toFloat(), mbr)
            .body()?.items ?: listOf()
//        dreameApi.getAllStoresNearbyUserForMarking()
//            .body()?.items ?: listOf()
    }

    override suspend fun getStoresNearbyUserByCategoryClicked(
        path: String,
        category: String?,
        subCategory: String?,
        storeType: String?,
        latLng: DreameLatLng,
        mbr: Int
    ) = withContext(Dispatchers.IO) {
        dreameApi.getStoresClickedCategoryName(
            path, category, subCategory, storeType, // subCategory가 필요한가?
            latLng.lat.toFloat(), latLng.lng.toFloat(), mbr
        ).body()?.items ?: listOf()
    }

    override suspend fun getStoresBySearchingKeyword(
        keyword: String,
        latLng: DreameLatLng,
        mbr: Int
    ): List<StoreDataOnBottomSheetList> = withContext(Dispatchers.IO) {
        dreameApi.searchByKeyword(
            keyword,
            latLng.lat.toFloat(),
            latLng.lng.toFloat(),
            mbr
        ).body()?.items ?: listOf()
    }


    override suspend fun getDetailStoreInfo(storeId: Int, storeType: String)= withContext(Dispatchers.IO) {

        // todo : storeId 파라미터로 api 호출
        dreameApi.getDetailSpotData(storeId, storeType).body()!!
    }

    override suspend fun getFavoriteStores(userId: String) = withContext(Dispatchers.IO) {
//        dreameApi.getFavoriteStores(userId).body()?.items ?: listOf()
        dreameApi.getFavoriteStores().body()?.items ?: listOf()
    }

}