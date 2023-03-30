package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName


data class StoreDataForMarkingResponse(
    @SerializedName("items")
    val items: List<StoreDataForMarking>
)

data class StoreDataByCategoryClickedResponse(
    @SerializedName("items")
    val items: List<StoreDataOnBottomSheetList>
)

data class StoreDataBySearchingKeywordResponse(
    @SerializedName("items")
    val items: List<StoreDataOnBottomSheetList>
)