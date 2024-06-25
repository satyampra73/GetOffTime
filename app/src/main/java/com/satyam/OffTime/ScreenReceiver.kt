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
                val formattedDuration = formatDuration(screenOffDuration)

                Log.d("strData", "Screen was off for $formattedDuration")
                Toast.makeText(context, "Time : $formattedDuration", Toast.LENGTH_LONG).show()

                val helper = DBHelper(context)
                helper.insertString("Time : $formattedDuration")
            }
        }
    }

    private fun formatDuration(durationMillis: Long): String {
        val seconds = durationMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
    }
}
