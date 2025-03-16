package com.example.cuongspotify.views.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuongspotify.configs.NetworkInstance
import com.example.cuongspotify.models.Album
import com.example.cuongspotify.models.BrowseCategory
import com.example.cuongspotify.models.MusicTrack
import com.example.cuongspotify.networks.HomeApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create

class HomeViewModal: ViewModel() {

    private val _musicTrack = MutableStateFlow(listOf<BrowseCategory>())
    val musicTrack = _musicTrack.asStateFlow()

    private val _musicTrackLoading = MutableStateFlow(false)
    val musicTrackLoading = _musicTrackLoading.asStateFlow()

    private val _albumNewRelease = MutableStateFlow(listOf<Album>())
    val albumNewRelease = _albumNewRelease.asStateFlow()

    private val _newReleaseAlbumLoading = MutableStateFlow(false)
    val newReleaseAlbumLoading = _newReleaseAlbumLoading.asStateFlow()

    fun fetchMusicTrack(context: Context) {
        val exceptionHandler = CoroutineExceptionHandler{_, exception ->
            run {
                Log.d("Exception", exception.toString())
            }
        }
        viewModelScope.launch(exceptionHandler) {
            _musicTrackLoading.value = true
            val result = withContext(Dispatchers.IO) {
                val homeApis = NetworkInstance.getInstance(context).create(HomeApi::class.java)
                return@withContext homeApis.getCategories().body()
            }
            _musicTrack.value = result?.categories?.items ?: listOf()
            _musicTrackLoading.value = false
        }
    }

    fun fetchNewReleaseAlbum(context: Context) {
        viewModelScope.launch {
            _newReleaseAlbumLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    val homeApis = NetworkInstance.getInstance(context).create(HomeApi::class.java)
                    return@withContext homeApis.getAlbumNewRelease().body()
                }
                result?.albums?.items?.let {
                    _albumNewRelease.value = it
                }
            } finally {
                _newReleaseAlbumLoading.value = false
            }
        }
    }
}