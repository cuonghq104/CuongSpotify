package com.example.cuongspotify.models

data class CategoryWrapper(
    val href: String,
    val items: List<BrowseCategory>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
) {
}