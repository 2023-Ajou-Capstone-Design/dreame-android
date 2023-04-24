package com.example.dreamixmlversion.data.api.response.entity

import com.google.gson.annotations.SerializedName

data class SharingDataItemEntity(
    @SerializedName("Photo1") val thumbnailImage: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Town") val town: String,
    @SerializedName("UploadTime") val uploadTime: String,
    @SerializedName("UserID") val userId: String,
    @SerializedName("WritingID") val writingId: String
)

data class SharingDetailInfo(
    @SerializedName("Photo1") val photo1: String,
    @SerializedName("Photo2") val photo2: String,
    @SerializedName("Photo3") val photo3: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Town") val town: String,
    @SerializedName("Contents") val contents: String,
    @SerializedName("UploadTime") val uploadTime: String,
    @SerializedName("UserID") val userId: String,
    @SerializedName("WritingID") val writingId: String
)

data class SharingRegister(
    val isSuccess: Boolean
)