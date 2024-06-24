package com.satyam.OffTime

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast


class ScreenTimeService : Service(){
    private var screenOffTime: Long = 0
    private var screenOffStartTime: Long = 0

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                screenOffStartTime = System.currentTimeMillis()
            } else if (intent.action == Intent.ACTION_SCREEN_ON) {
                val currentTime = System.currentTimeMillis()
                if (screenOffStartTime != 0L) {
                    screenOffTime += currentTime - screenOffStartTime
                }
                screenOffStartTime = 0
                showToast(context)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showToast(context: Context) {
        val totalTimeInSeconds = screenOffTime / 1000
        Toast.makeText(context, "Screen was off for $totalTimeInSeconds seconds", Toast.LENGTH_LONG).show()
    }

}