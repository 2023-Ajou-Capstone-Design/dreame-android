package com.example.dreamixmlversion.data.repository

import android.graphics.Bitmap
import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.data.api.response.entity.SharingRegister
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
) : SharingRepository {

    override suspend fun getSharingListInfo(
        town: String
    ): List<SharingDataItemEntity> {
        return dreameApi.getSharingInfo(
            town
        ).body()?.items ?: listOf()
    }

    override suspend fun getDetailSharingInfo(
        userId: String,
        writingId: String
    ): SharingDetailInfo? {
        return dreameApi.getSharingDetailInfo(userId, writingId).body()?.items?.first()
    }

    override suspend fun registerNewSharing(
        userId: String,
        title: String,
        content: String,
        images: List<Bitmap>?,
        town: String
    ): Boolean {
        return dreameApi.registerNewSharing(
            userId, title, content, null, null, null, town
        )
    }
}