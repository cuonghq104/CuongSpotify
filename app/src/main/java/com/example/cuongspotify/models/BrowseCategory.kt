package com.example.cuongspotify.models

data class BrowseCategory(
    val href: String,
    val id: String,
    val name: String,
    val icons: List<CategoryIcon>
): HomeListItem {
    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return ""
    }

    override fun getImageUrl(): String {
        return icons[0].url
    }

}