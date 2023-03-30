package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName


data class StoreMarkingResponse(
    @SerializedName("items")
    val items: List<StoreDataForMarking>
)