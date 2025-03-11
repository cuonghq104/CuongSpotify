package com.example.cuongspotify.networks

import com.example.cuongspotify.models.BrowseCategoryResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {
    @GET("browse/categories")
    suspend fun getCategories(): Response<BrowseCategoryResponseBody>
}