package com.example.dreamixmlversion.data.repository

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.ui.map.CategoryItem
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dreameApi: DreameApi
): CategoryRepository {

    override suspend fun getAllCategories(): List<CategoryItem> {
        return dreameApi.getAllCategories()
    }
}