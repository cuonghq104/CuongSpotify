package com.example.cuongspotify.networks

import com.example.cuongspotify.configs.AppConstants.SPOTIFY_SECRET_ENCODED
import com.example.cuongspotify.views.screens.auth.models.SpotifyAccessTokenResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SpotifyLoginApi {
    @FormUrlEncoded
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "Authorization: Basic $SPOTIFY_SECRET_ENCODED"
    )
    @POST("token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Response<SpotifyAccessTokenResponseBody>

}