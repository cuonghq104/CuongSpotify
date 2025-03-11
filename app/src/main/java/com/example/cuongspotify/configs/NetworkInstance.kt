package com.example.cuongspotify.configs
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkInstance {
    private var INSTANCE: Retrofit? = null
    private var SPOTIFY_AUTH_INSTANCE: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        return INSTANCE ?: synchronized(this) {
            val instance = Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(getOkHttpClient(context.applicationContext)) // Use application context
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            INSTANCE = instance
            instance
        }
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val sp = context.getSharedPreferences(AppConstants.SP_NAME, Context.MODE_PRIVATE)
                val token = sp.getString(AppConstants.SP_KEY_ACCESS_TOKEN, "")
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token") // Always fetch latest token
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val getSpotifyAuthInstance: Retrofit by lazy {
        run {
            Retrofit.Builder()
                .baseUrl(AppConstants.SPOTIFY_AUTH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}