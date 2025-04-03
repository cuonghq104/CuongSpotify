package com.example.cuongspotify.configs

object AppConstants {
    const val BASE_URL = "https://api.spotify.com/v1/"
    const val SPOTIFY_AUTH_BASE_URL = "https://accounts.spotify.com/api/"
    const val SPOTIFY_LOGIN_BASE_URL = "https://accounts.spotify.com/authorize"
    const val SPOTIFY_CLIENT_ID = "3ea2beca1c2140bb9678a2343bfad3c2"
    const val SPOTIFY_LOGIN_SCOPE = "user-top-read user-follow-read user-read-private user-read-email playlist-read-private"
    const val SPOTIFY_LOGIN_REDIRECT_URL = "https://www.google.com/"
    const val SPOTIFY_LOGIN_RESPONSE_TYPE = "code"
    const val SPOTIFY_SECRET_ENCODED = "M2VhMmJlY2ExYzIxNDBiYjk2NzhhMjM0M2JmYWQzYzI6NmQ1MjJmMGY4ZWUzNDVkN2I2YmRjOGRkNzAxMGNjZDE="
    fun getSpotifyLoginUrl(): String {
        return "$SPOTIFY_LOGIN_BASE_URL?response_type=$SPOTIFY_LOGIN_RESPONSE_TYPE&client_id=$SPOTIFY_CLIENT_ID&scope=$SPOTIFY_LOGIN_SCOPE&redirect_uri=$SPOTIFY_LOGIN_REDIRECT_URL"
    }

    const val BROADCAST_FILTER = "com.example.snippets.ACTION_UPDATE_DATA"
    const val SP_NAME = "Cuong_Spotify"
    const val SP_KEY_ACCESS_TOKEN = "access_token"
}