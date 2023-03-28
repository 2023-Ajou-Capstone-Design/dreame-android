package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName


data class StoreDataResponse(
    @SerializedName("items")
    val items: List<StoreDataEntityItem>
)