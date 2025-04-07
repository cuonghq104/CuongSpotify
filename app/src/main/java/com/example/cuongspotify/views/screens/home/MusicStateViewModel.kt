package com.example.cuongspotify.views.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicStateViewModel: ViewModel() {

    private var _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private var _duration = MutableStateFlow(0)
    val duration = _duration.asStateFlow()

    private var _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    fun setPlayingState(isPlaying: Boolean) {
         _isPlaying.value = isPlaying
    }

    fun setDuration(duration: Int) {
        _duration.value = duration
    }

    fun setProgress(progress: Int) {
        _progress.value = progress
    }
}