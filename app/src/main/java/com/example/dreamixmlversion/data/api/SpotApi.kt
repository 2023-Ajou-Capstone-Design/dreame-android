package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.entity.StoreDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface SpotApi {

    // 사용자 위치를 기준으로 반경 2km 내의 모든 지점들 get
    @POST("v3/cd3c2c46-a4e3-4e92-ae54-f69873e041a5")
    suspend fun getAllSpotsNearbyUser(): Response<StoreDataResponse>

    // 특정 지점의 detail 정보 get (by storeID)
    suspend fun getDetailSpotData(): String

    // 사용자 위치를 기준으로 반경 2km 내의 특정 카테고리에 해당하는 모든 지점들 filter
    suspend fun getAllCategories(): String

    // db 업데이트 및 spots 최신화
    suspend fun updateFavoriteSpot()

    // db에 keyword 검색
    suspend fun searchByKeyword(): String
}