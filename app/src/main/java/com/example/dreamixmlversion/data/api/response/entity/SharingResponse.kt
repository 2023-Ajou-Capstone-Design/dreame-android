package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName

data class SharingListResponse(
    @SerializedName("items")
    val items: List<SharingDataItemEntity>
)

data class SharingDetailResponse(
    @SerializedName("items")
    val items: List<SharingDetailInfo>
)