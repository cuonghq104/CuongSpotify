package com.example.cuongspotify.views.screens.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class DemoBoundService : Service(){
    inner class ServiceBinder: Binder() {
        fun getService() = this@DemoBoundService
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("DemoBoundService", "onBind")
        return ServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d("DemoBoundService", "onDestroy")
        super.onDestroy()
    }

    fun sendData(msg: String) {
        Log.d("DemoBoundService", "sendData:${msg}")
    }
}