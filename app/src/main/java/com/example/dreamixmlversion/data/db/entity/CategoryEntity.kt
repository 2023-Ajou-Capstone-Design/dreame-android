package com.example.dreamixmlversion.data.db.entity

enum class CategoryEntity(
    val path: String,
    val category: String?,
    val subCategory: String?,
    // 01: 식음료, 02: 마트, 04: 교육, 05: 생활, 99: 기타
    val storeType: String?, // 01: 선한영향력 가게, 02: 가맹점
    val categoryName: String
) {
    CATEGORY_FOOD("Category", "1", null, null,"음식점"),
    CATEGORY_CAFE("SubCategory", "1", null, null,"카페"),
    CATEGORY_CONVENIENCE_STORE("SubCategory", "2", "4", null, "편의점"),
    CATEGORY_MART("Category", "2", null, null, "마트"),
    CATEGORY_EDUCATION("Category", "4", null, null, "교육"),
    CATEGORY_FRANCHISEE("StoreType", null,null, "2", "아동급식카드 가맹점"),
    CATEGORY_GOOD_INFLUENCE("StoreType", null, null, "2", "선한영향력 가게");

    companion object {
        fun getMainCategories(): List<CategoryEntity> = values().toList()
    }
}