package com.example.dreamixmlversion.data.api.response.model.sharing

import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.model.SharingDetailInfo
import com.google.gson.annotations.SerializedName

data class SharingListResponse(
    @SerializedName("items")
    val items: List<SharingDataItemEntity>
)

data class SharingDetailResponse(
    @SerializedName("items")
    val items: List<SharingDetailInfo>
)