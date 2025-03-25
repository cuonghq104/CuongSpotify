package com.example.cuongspotify.models

import com.google.gson.annotations.SerializedName

data class MusicTrack(
    val album: Album,
    val artists: List<Artist>,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val href: String,
    val id: String,
    val name: String
): HomeListItem {
    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return artists.joinToString(", ") {
            it.name
        }
    }

    override fun getImageUrl(): String {
        return album.getImageUrl()
    }

}