package com.example.cuongspotify.networks

import com.example.cuongspotify.models.AlbumNewReleaseResponseBody
import com.example.cuongspotify.models.BrowseCategoryResponseBody
import com.example.cuongspotify.models.TopTrackResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {
    @GET("browse/categories")
    suspend fun getCategories(): Response<BrowseCategoryResponseBody>

    @GET("browse/new-releases")
    suspend fun getAlbumNewRelease(): Response<AlbumNewReleaseResponseBody>

    @GET("me/top/tracks")
    suspend fun getTopTrack(): Response<TopTrackResponseBody>
}