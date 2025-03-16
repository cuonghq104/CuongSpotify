package com.example.cuongspotify.models

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("album_type") val albumType: String,
    val artists: List<Artist>,
    @SerializedName("total_tracks") val totalTracks: Int,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val id: String,
    val images: List<CategoryIcon>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    val type: String
): HomeListItem {
    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return artists[0].name
    }

    override fun getImageUrl(): String {
        return images[0].url
    }

}
