package com.example.dreamixmlversion.data.repository

import android.graphics.Bitmap
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.data.api.response.entity.SharingRegister
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import java.util.Date


interface SharingRepository {

    suspend fun getSharingListInfo(
        town: String
    ): List<SharingDataItemEntity>

    suspend fun getDetailSharingInfo(
        userId: String,
        writingId: String
    ): SharingDetailInfo?

    suspend fun registerNewSharing(
        userId: String,
        title: String,
        content: String,
        images: List<Bitmap>?,
        town: String
    ): Boolean
}