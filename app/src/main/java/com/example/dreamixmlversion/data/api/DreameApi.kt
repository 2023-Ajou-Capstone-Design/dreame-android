package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.entity.*
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DreameApi {

    @POST("map/MyPosition")
//    @POST("v3/99a78bae-47e1-42f1-8421-5d90c4c8a28e")
    suspend fun getAllStoresNearbyUserForMarking(
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<StoreDataForMarkingResponse>

    @POST("map/Choose/{Path}")
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
    @POST("map/KeywordSearch")
//    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun searchByKeyword(
        @Query("Keyword") keyword: String,
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<StoreDataBottomSheetListResponse>

    // 특정 지점의
    // detail 정보 get (by storeID)
    @POST("map/StoreDetail")
    suspend fun getDetailSpotData(
        @Query("StoreID") storeId: Int,
        @Query("StoreType") storeType: String
    ): Response<StoreDetailInfoResponse>

    // 사용자 위치를 기준으로 반경 2km 내의 특정 카테고리에 해당하는 모든 지점들 filter

    // db 업데이트 및 spots 최신화
    @POST("Bookmark/list")
//    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun getFavoriteStores(
        @Query("UserID") userId: String
    ): Response<StoreDataBottomSheetListResponse>

    @POST("Bookmark/{path}")
    suspend fun updateFavorite(
        @Path("path") path: String,
        @Query("StoreID") storeId: Int,
        @Query("StoreType") storeType: String,
        @Query("UserID") userId: String
    ): Boolean

    @POST("FoodShare/getList")
    suspend fun getSharingInfo(
        @Query("Town") town: String,
    ): Response<SharingListResponse>

    @POST("FoodShare/Detail")
    suspend fun getSharingDetailInfo(
        @Query("UserID") userId: String,
        @Query("WritingID") writingId: String
    ): Response<SharingDetailResponse>

    @POST("FoodShare/add")
    suspend fun registerNewSharing(
        @Query("UserID") userId: String,
        @Query("Title") title: String,
        @Query("contents") content: String,
        @Query("Photo1") photo1: String? = null,
        @Query("Photo2") photo2: String? = null,
        @Query("Photo3") photo3: String? = null,
        @Query("Town") town: String
        ): Boolean

//    @POST("")
//    suspend fun checkDuplicateNickname(
//        @Query("") nickname: String
//    ): Boolean
}