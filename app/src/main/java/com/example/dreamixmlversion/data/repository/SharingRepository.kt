package com.example.dreamixmlversion.data.repository

import android.graphics.Bitmap
import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.model.SharingDetailInfo


interface SharingRepository {

    suspend fun getSharingListInfoByTown(
        town: String
    ): List<SharingDataItemEntity>

    suspend fun getMySharingListInfo(
        userId: String
    ): List<SharingDataItemEntity>

    suspend fun getDetailSharingInfo(
        userId: String,
        writingId: String
    ): SharingDetailInfo?

    suspend fun registerNewSharing(
        userId: String,
        title: String,
        content: String,
        images: List<Bitmap>,
        town: String
    ): Boolean

    suspend fun modifySharing(
        userId: String,
        title: String,
        content: String,
        images: List<Bitmap>,
        writingId: String,
        town: String
    ): Boolean

    suspend fun deleteSharing(
        userId: String,
        writingId: String
    ): Boolean
}