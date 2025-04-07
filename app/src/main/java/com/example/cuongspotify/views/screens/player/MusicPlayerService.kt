package com.example.cuongspotify.views.screens.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.cuongspotify.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicPlayerService : Service() {

    private val isPlaying: Boolean = false
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private var mediaPlayer: MediaPlayer? = null
    private var jobUpdateProgress: Job? = null

    private fun sendDurationBroadcast(duration: Int) {
        val durationData = Bundle().apply {
            putString(BROADCAST_PARAMS_ACTION, BroadcastAction.UPDATE_DURATION.value)
            putInt(BROADCAST_PARAMS_DURATION, duration)
        }
        sendMusicBroadcast(durationData)
    }

    private fun sendPlayStateBroadcast(state: Boolean) {
        val data = Bundle().apply {
            putString(BROADCAST_PARAMS_ACTION, BroadcastAction.UPDATE_PLAY_STATE.value)
            putBoolean(BROADCAST_PARAMS_MUSIC_IS_PLAYING, state)
        }
        sendMusicBroadcast(data)
    }

    private fun sendProgressBroadcast(progress: Int) {
        val data = Bundle().apply {
            putString(BROADCAST_PARAMS_ACTION, BroadcastAction.UPDATE_PROGRESS.value)
            putInt(BROADCAST_PARAMS_PROGRESS, progress)
        }
        sendMusicBroadcast(data)
    }

    private fun launchProgressUpdateJob(mediaPlayer: MediaPlayer) {
        jobUpdateProgress = CoroutineScope(Dispatchers.Default).launch {
            while (isActive && mediaPlayer.isPlaying) {
                sendProgressBroadcast(mediaPlayer.currentPosition)
                delay(500)
            }
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getIntExtra(INTENT_KEY_COMMAND, 0) ?: 0
        if (command == ServiceCommand.START.value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "music_channel",
                    "Music Playback",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for music playback notifications"
                }
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(this, "music_channel")
                    .setContentTitle("Music Player")
                    .setContentText("Playing Music")
                    .setSmallIcon(R.drawable.baseline_arrow_left_24)
                    .build()
                notificationManager.notify(100, notification)
                ServiceCompat.startForeground(
                    this,
                    100,
                    notification,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                    } else {
                        0
                    },
                )
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(audioAttributes)
                setDataSource("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3")
                prepareAsync()
                setOnPreparedListener {
                    start()
                    sendPlayStateBroadcast(true)
                    sendDurationBroadcast(it.duration)
                    launchProgressUpdateJob(it)
                }
            }

        } else if (command == ServiceCommand.PAUSE.value) {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                }
                sendPlayStateBroadcast(false)
                jobUpdateProgress?.cancel()
            }
        } else if (command == ServiceCommand.RESUME.value) {
            mediaPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                }
                launchProgressUpdateJob(it)
            }

            sendPlayStateBroadcast(true)
        } else if (command == ServiceCommand.SEEK_TO.value) {
            val seekToValue = intent?.getIntExtra(SERVICE_PARAMS_SEEK_TO, 0) ?: 0
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                }
                it.seekTo(seekToValue)
                it.start()
            }
        }
        else {
            mediaPlayer?.let {
                it.release()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendMusicBroadcast(data: Bundle) {
        val intent = Intent("com.example.snippets.ACTION_UPDATE_DATA")
        intent.putExtras(data)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    enum class ServiceCommand(val value: Int) {
        START(1),
        PAUSE(2),
        RESUME(3),
        SEEK_TO(4)
    }

    enum class BroadcastAction(val value: String) {
        UPDATE_PLAY_STATE("is_playing"),
        UPDATE_DURATION("duration"),
        UPDATE_PROGRESS("progress")
    }

    companion object {
        const val INTENT_KEY_COMMAND = "command"

        const val SERVICE_PARAMS_SEEK_TO = "seek_to_value"

        const val BROADCAST_PARAMS_MUSIC_IS_PLAYING = "is_playing"
        const val BROADCAST_PARAMS_DURATION = "duration"
        const val BROADCAST_PARAMS_PROGRESS = "progress"
        const val BROADCAST_PARAMS_ACTION = "broadcast_action"
    }
}