package com.example.cuongspotify.views.screens.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.cuongspotify.R

class MusicPlayerService : Service() {

    private val isPlaying: Boolean = false
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra("command") ?: ""
        if (command == "start") {
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
                }
            }

        } else if (command == "stop") {
            mediaPlayer?.let {
                it.release()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}