package com.satyam.OffTime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.satyam.OffTime.db.DBHelper
import com.satyam.OffTime.model.Data
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScreenReceiver : BroadcastReceiver() {
    companion object {
        var screenOffTime: Long = 0L
        var screenOnTime: Long = 0L
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

                val data = Data(getTodayDate(), formattedDuration)
                val helper = DBHelper(context)
                helper.insertString(data)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayDate(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return today.format(formatter)
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
