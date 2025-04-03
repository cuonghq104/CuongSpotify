package com.example.cuongspotify.views.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicStateViewModel: ViewModel() {

    private var _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying

    fun setPlayingState(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }
}