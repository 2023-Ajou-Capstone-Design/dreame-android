package com.example.dreamixmlversion.data.db.entity

enum class CategoryEntity(
    // 01: 식음료, 02: 마트, 04: 교육, 05: 생활, 99: 기타
    val storeType: String,
    val categoryId: String?,
    val categoryName: String?
) {
    CATEGORY_FOOD("04", "01", "식음료"),
    CATEGORY_MART("04", "02", "마트"),
    CATEGORY_EDUCATION("04", "04", "교육"),
    CATEGORY_LIFE("04", "05", "생활"),
    CATEGORY_FRANCHISEE("01", null, "아동급식카드 가맹점"),
    CATEGORY_GOOD_INFLUENCE("02", null, "선한영향력 가게");

    companion object {
        fun getMainCategories(): List<CategoryEntity> = values().toList()
    }
}