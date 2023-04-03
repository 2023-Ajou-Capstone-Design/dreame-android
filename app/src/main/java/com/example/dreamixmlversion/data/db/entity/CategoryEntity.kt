package com.example.dreamixmlversion.data.db.entity

enum class CategoryEntity(
    val category: Int,
    val subCategory: Int,
    // 01: 식음료, 02: 마트, 04: 교육, 05: 생활, 99: 기타
    val storeType: Int,
    val categoryName: String?
) {
    CATEGORY_FOOD(1, 0, 3,"음식점"),
    CATEGORY_CAFE(1, 0, 3,"카페"),
    CATEGORY_CONVENIENCE_STORE(2, 4, 3, "편의점"),
    CATEGORY_MART(2, 0, 3, "마트"),
    CATEGORY_EDUCATION(4, 0, 3, "교육"),
    CATEGORY_FRANCHISEE(0, 0,2, "아동급식카드 가맹점"),
    CATEGORY_GOOD_INFLUENCE(0, 0, 1, "선한영향력 가게");

    companion object {
        fun getMainCategories(): List<CategoryEntity> = values().toList()
    }
}