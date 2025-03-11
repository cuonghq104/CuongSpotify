package com.example.cuongspotify.models

data class BrowseCategory(
    val href: String,
    val id: String,
    val name: String,
    val icons: List<CategoryIcon>
) {

}