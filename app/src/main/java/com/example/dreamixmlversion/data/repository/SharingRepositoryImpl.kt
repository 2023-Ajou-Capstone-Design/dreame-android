package com.example.dreamixmlversion.data.repository

import android.graphics.Bitmap
import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.model.SharingDetailInfo
import com.example.dreamixmlversion.extension.encodeToBase64
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
) : SharingRepository {

    override suspend fun getSharingListInfoByTown(
        town: String
    ): List<SharingDataItemEntity> {
        return dreameApi.getSharingInfo(
            town
        ).body()?.items ?: listOf()
    }

    override suspend fun getMySharingListInfo(userId: String): List<SharingDataItemEntity> {
        return dreameApi.inquireMySharing(userId).body()?.items ?: listOf()
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
        images: List<Bitmap>,
        town: String
    ): Boolean {

        var photo1: String ?= null
        var photo2: String ?= null
        var photo3: String ?= null

        when (images.size) {
            1 -> {
                photo1 = images[0].encodeToBase64()
            }
            2 -> {
                photo1 = images[0].encodeToBase64()
                photo2 = images[1].encodeToBase64()
            }
            3 -> {
                photo1 = images[0].encodeToBase64()
                photo2 = images[1].encodeToBase64()
                photo3 = images[2].encodeToBase64()
            }
        }

        return dreameApi.registerNewSharing(
            userId, title, content, photo1, photo2, photo3, town
        )
    }
}