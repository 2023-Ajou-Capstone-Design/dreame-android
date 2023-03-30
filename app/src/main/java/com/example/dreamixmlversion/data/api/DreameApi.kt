package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.entity.StoreMarkingResponse
import com.example.dreamixmlversion.ui.map.CategoryItem
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface DreameMapApi {

    // 사용자 위치를 기준으로 반경 2km 내의 모든 지점들 get
//    @POST("/MyPosition?myPositionLng={myPositionLng}&myPositionLng={myPositionLat}")
    @POST("v3/9e9a3e9c-e545-4299-bb11-f64d48d2cdbd")
    suspend fun getAllStoresNearbyUserForMarking(
//        @Path("myPositionLat") myPositionLat: Float,
//        @Path("myPositionLng") myPositionLng: Float,
//        mbr: Int
    ): Response<StoreMarkingResponse>

    // 특정 지점의 detail 정보 get (by storeID)
    suspend fun getDetailSpotData(): Response<DetailInfoItem>

    // 사용자 위치를 기준으로 반경 2km 내의 특정 카테고리에 해당하는 모든 지점들 filter
    suspend fun getAllCategories(): List<CategoryItem>

    // db 업데이트 및 spots 최신화
    suspend fun updateFavoriteSpot()

    // db에 keyword 검색
    suspend fun searchByKeyword(): String
}