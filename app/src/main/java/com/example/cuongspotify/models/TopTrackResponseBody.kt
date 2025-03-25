package com.example.cuongspotify.models

data class TopTrackResponseBody(
    val items: List<MusicTrack>,
    val total: Int,
    val limit: Int,
    val offset: Int
)
