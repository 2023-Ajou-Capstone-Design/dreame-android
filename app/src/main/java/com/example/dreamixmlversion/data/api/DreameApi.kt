package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.entity.*
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface DreameApi {

//    @POST("/MyPosition?myPositionLng={myPositionLng}&myPositionLng={myPositionLat}")
    @POST("v3/99a78bae-47e1-42f1-8421-5d90c4c8a28e")
    suspend fun getAllStoresNearbyUserForMarking(
//        @Path("myPositionLat") myPositionLat: Float,
//        @Path("myPositionLng") myPositionLng: Float,
//        mbr: Int
    ): Response<StoreDataForMarkingResponse>

//    @POST("/Category?StoreType={StoreType}&Category={Category}&SubCategory={SubCategory}&myPositionLat={myPositionLat}&myPositionLng={myPositionLng}&mbr={mbr}")
    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun getStoresClickedCategoryName(
//        @Path("StoreType") storeType: String,
//        @Path("Category") category: String,
//        @Path("SubCategory") subCategory: String,
//        @Path("myPositionLat") myPositionLat: Float,
//        @Path("myPositionLng") myPositionLng: Float,
//        mbr: Int,
    ): Response<StoreDataBottomSheetListResponse>

    // db에 keyword 검색
//    @POST("/KeywordSearch?Keyword={Keyword}&myPositionLat={myPositionLat}&myPositionLng={myPositionLng}&mbr={mbr}")
    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun searchByKeyword(
//        @Path("Keyword") keyword: String,
//        @Path("myPositionLat") myPositionLat: Float,
//        @Path("myPositionLng") myPositionLng: Float,
//        @Path("mbr") mbr: Int
    ): Response<StoreDataBottomSheetListResponse>

    // 특정 지점의
    // detail 정보 get (by storeID)
//    @POST("/StoreID?StoreID={StoreID}&StoreType={StoreType}")
    suspend fun getDetailSpotData(
//        @Path("StoreID") storeId: Int,
//        @Path("StoreType") storeType: String
    ): Response<DetailInfoItem>

    // 사용자 위치를 기준으로 반경 2km 내의 특정 카테고리에 해당하는 모든 지점들 filter

    // db 업데이트 및 spots 최신화
//    @POST("Bookmark?userId={userId}")
    @POST("v3/7b43db1b-b928-4b38-a3d5-60675c6ad58b")
    suspend fun getFavoriteStores(
//        @Path("userId") userId: String
    ): Response<StoreDataBottomSheetListResponse>

    suspend fun getSharingInfo(): Response<SharingListResponse>

    suspend fun getSharingDetailInfo(): SharingDetailInfo

    suspend fun registerNewSharing(): SharingRegister

}