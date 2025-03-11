package com.example.cuongspotify.views.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuongspotify.configs.AppConstants
import com.example.cuongspotify.configs.NetworkInstance
import com.example.cuongspotify.networks.SpotifyLoginApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel: ViewModel() {
    private val _loggedInResult = MutableSharedFlow<String>()
    val loggedInResult = _loggedInResult.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun getAccessTokenFromCode(code: String, context: Context) {

        viewModelScope.launch {
            _loading.value = true
            val spotifyLoginApi = NetworkInstance.getSpotifyAuthInstance.create(SpotifyLoginApi::class.java)

            val tokenResult = withContext(Dispatchers.IO) {
                val result = spotifyLoginApi.getAccessToken("authorization_code", code, AppConstants.SPOTIFY_LOGIN_REDIRECT_URL)
                return@withContext result.body()
            }

            _loading.value = false

            tokenResult?.let {
                withContext(Dispatchers.IO) {
                    val sp = context.getSharedPreferences(AppConstants.SP_NAME, Context.MODE_PRIVATE)
                    val editor = sp.edit()
                    editor.putString(AppConstants.SP_KEY_ACCESS_TOKEN, it.accessToken)
                    editor.apply()
                }
                _loggedInResult.emit("Success")
            }
        }
    }
}