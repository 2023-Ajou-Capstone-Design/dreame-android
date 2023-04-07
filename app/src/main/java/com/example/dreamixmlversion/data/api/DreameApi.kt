package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.entity.*
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DreameApi {

    @POST("/MyPosition")
//    @POST("v3/99a78bae-47e1-42f1-8421-5d90c4c8a28e")
    suspend fun getAllStoresNearbyUserForMarking(
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<StoreDataForMarkingResponse>

    @POST("/Choose/{Path}")
//    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun getStoresClickedCategoryName(
        @Path("Path") path: String,
        @Query("Category") category: String ?= null,
        @Query("SubCategory") subCategory: String ?= null,
        @Query("StoreType") storeType: String ?= null,
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int,
    ): Response<StoreDataBottomSheetListResponse>

    // db에 keyword 검색
    @POST("/KeywordSearch")
//    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun searchByKeyword(
        @Query("Keyword") keyword: String,
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<StoreDataBottomSheetListResponse>

    // 특정 지점의
    // detail 정보 get (by storeID)
    @POST("/StoreDetail")
    suspend fun getDetailSpotData(
        @Query("StoreID") storeId: Int,
        @Query("StoreType") storeType: String
    ): Response<DetailInfoItem>

    // 사용자 위치를 기준으로 반경 2km 내의 특정 카테고리에 해당하는 모든 지점들 filter

    // db 업데이트 및 spots 최신화
//    @POST("Bookmark?userId={userId}")
    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun getFavoriteStores(
//        @Path("userId") userId: String
    ): Response<StoreDataBottomSheetListResponse>

    @POST("FoodSharingList")
    suspend fun getSharingInfo(
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<SharingListResponse>

    suspend fun getSharingDetailInfo(): SharingDetailInfo

    suspend fun registerNewSharing(): SharingRegister

}