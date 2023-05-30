package com.example.dreamixmlversion.data.api

import com.example.dreamixmlversion.data.api.response.model.*
import com.example.dreamixmlversion.data.api.response.model.login.*
import com.example.dreamixmlversion.data.api.response.model.sharing.SharingDetailResponse
import com.example.dreamixmlversion.data.api.response.model.sharing.SharingListResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.function.BinaryOperator

interface DreameApi {

    @POST("map/MyPosition")
    suspend fun getAllStoresNearbyUserForMarking(
        @Query("myPositionLat") myPositionLat: Float,
        @Query("myPositionLng") myPositionLng: Float,
        @Query("mbr") mbr: Int
    ): Response<StoreDataForMarkingResponse>

    @POST("map/Choose/{Path}")
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
    suspend fun getFavoriteStores(
        @Query("UserID") userId: String
    ): Response<StoreDataBottomSheetListResponse>

    @POST("Bookmark/add")
    suspend fun addBookmark(
        @Query("StoreID") storeId: Int,
        @Query("StoreType") storeType: String,
        @Query("UserID") userId: String
    ): Boolean

    @POST("Bookmark/del")
    suspend fun delBookmark(
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
        @Query("Contents") content: String,
        @Query("Photo1") photo1: String? = null,
        @Query("Photo2") photo2: String? = null,
        @Query("Photo3") photo3: String? = null,
        @Query("Town") town: String
        ): Boolean

    @POST("FoodShare/Modify")
    suspend fun modifySharing(
        @Query("UserID") userId: String,
        @Query("Title") title: String,
        @Query("contents") content: String,
        @Query("Photo1") photo1: String? = null,
        @Query("Photo2") photo2: String? = null,
        @Query("Photo3") photo3: String? = null,
        @Query("WritingID") writingId: String,
        @Query("Town") town: String
    ): Boolean

    @POST("FoodShare/del")
    suspend fun deleteSharing(
        @Query("UserID") userId: String,
        @Query("WritingID") writingId: String
    ): Boolean


    @POST("MyPage/Card")
    suspend fun getMyRestPoint(
        @Query("UserID") userId: String
    ): Response<String>

//    @POST("")
//    suspend fun checkDuplicateNickname(
//        @Query("") nickname: String
//    ): Boolean

    @POST("City/Do")
    suspend fun getFirstRegionNameList(): Response<DoResponse>

    @POST("City/Si")
    suspend fun getSecondRegionNameList(
        @Query("Do") townDo: String
    ): Response<SiResponse>

    @POST("City/GunGu")
    suspend fun getThirdRegionNameList(
        @Query("Do") townDo: String,
        @Query("Si") townSi: String
    ): Response<GunGuResponse>

    @POST("City/Dong")
    suspend fun getFourthRegionNameList(
        @Query("Do") townDo: String,
        @Query("Si") townSi: String,
        @Query("GunGu") townGunGu: String
    ): Response<DongResponse>

    @POST("LogIn")
    suspend fun registerUserProfile(
        @Query("UserPhoto") userPhoto: String? = null,
        @Query("UserID") userId: String,
        @Query("Card") childCardNumber: String ?= null,
        @Query("userType") userType: String,
        @Query("Town") town: String,
        @Query("AKA") aka: String
    ): Boolean

    @POST("MyPage/AKA")
    suspend fun changeNickname(
        @Query("UserID") userId: String,
        @Query("AKA") nickname: String
    ): Response<String>

    @POST("MyPage/myList")
    suspend fun inquireMySharing(
        @Query("UserID") userId: String
    ): Response<SharingListResponse>

    @POST("MyPage/Town")
    suspend fun updateTown(
        @Query("UserID") userId: String,
        @Query("Town") town: String
    ): Response<String>
}