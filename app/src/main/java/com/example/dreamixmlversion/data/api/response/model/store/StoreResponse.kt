package com.example.dreamixmlversion.data.api.response.model

import com.google.gson.annotations.SerializedName


data class StoreDataForMarkingResponse(
    @SerializedName("items")
    val items: List<StoreDataForMarking>,
    @SerializedName("total")
    val total: Int
)

data class StoreDataBottomSheetListResponse(
    @SerializedName("items")
    val items: List<StoreDataOnBottomSheetList>
)

data class StoreDetailInfoResponse(
    @SerializedName("items")
    val items: List<DetailInfoItem>
)

