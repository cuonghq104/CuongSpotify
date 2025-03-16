package com.example.cuongspotify.models

data class AlbumNewReleaseWrapper(
    val href: String,
    val items: List<Album>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)
