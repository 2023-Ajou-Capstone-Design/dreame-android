package com.example.dreamixmlversion.data.api.response.mapper

import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem

// csv 형태의 string을 spotDataEntity로 변환해준다.
fun String.processToList(): List<StoreDataEntityItem> {
    val processed = mutableListOf<StoreDataEntityItem>()
    return processed
}

