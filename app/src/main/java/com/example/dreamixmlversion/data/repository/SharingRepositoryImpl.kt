package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.data.api.response.entity.SharingRegister
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): SharingRepository {

    override suspend fun getSharingListInfo(
        latLng: DreameLatLng,
        mbr: Int
    ): List<SharingDataItemEntity> {
        return dreameApi.getSharingInfo(
            latLng.lat.toFloat(), latLng.lng.toFloat(), mbr
        ).body()?.items ?: listOf()
    }

    override suspend fun getDetailSharingInfo(
        userId: String,
        writingId: String
    ): SharingDetailInfo {
        return dreameApi.getSharingDetailInfo()
    }

    override suspend fun registerNewSharing(
        userId: String,
        writingId: String,
        title: String,
        content: String,
        photo1: String,
        photo2: String,
        photo3: String
    ): SharingRegister {
        return dreameApi.registerNewSharing()
    }
}