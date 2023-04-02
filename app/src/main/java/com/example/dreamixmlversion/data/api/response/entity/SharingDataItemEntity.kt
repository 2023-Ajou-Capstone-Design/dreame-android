package com.example.dreamixmlversion.data.api.response.entity

data class SharingDataItemEntity(
    val image: String,
    val title: String,
    val address: String,
//    val time: Date
)

data class SharingDetailInfo(
    val userId: String,
    val writingId: String,
    val photo1: String,
    val photo2: String,
    val photo3: String,
    val aka: String,
    val town: String,
    val title: String,
    val content: String,
    val uploadTime: String
)

data class SharingRegister(
    val isSuccess: Boolean
)