package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.data.api.response.entity.SharingRegister
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import java.util.Date


interface SharingRepository {

    suspend fun getSharingListInfo(
        latLng: DreameLatLng,
        mbr: Int
    ): List<SharingDataItemEntity>

    suspend fun getDetailSharingInfo(
        userId: String,
        writingId: String
    ): SharingDetailInfo

    suspend fun registerNewSharing(
        userId: String,
        writingId: String, // timeStamp
        title: String,
        content: String,
        photo1: String,
        photo2: String,
        photo3: String,
//        uploadTime: Date
    ): SharingRegister
}