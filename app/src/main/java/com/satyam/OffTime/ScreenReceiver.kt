package com.satyam.OffTime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.satyam.OffTime.db.DBHelper

class ScreenReceiver : BroadcastReceiver() {
    companion object {
        var screenOffTime: Long = 0L
        var screenOnTime: Long = 0L
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                screenOffTime = System.currentTimeMillis()
            }
            Intent.ACTION_SCREEN_ON -> {
                screenOnTime = System.currentTimeMillis()
                val screenOffDuration = screenOnTime - screenOffTime
                // Do something with the screenOffDuration
                Log.d("strData", "Screen was off for ${screenOffDuration / 1000} seconds")
                Toast.makeText(context, "Screen was off for ${screenOffDuration / 1000} seconds", Toast.LENGTH_LONG).show()
                val helper = DBHelper(context)
                helper.insertString("Screen was off for ${screenOffDuration / 1000} seconds")
            }
        }
    }
}
