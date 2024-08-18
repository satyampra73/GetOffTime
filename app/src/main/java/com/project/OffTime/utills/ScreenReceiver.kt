package com.project.OffTime.utills

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.project.OffTime.db.DBHelper
import com.project.OffTime.model.Data
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScreenReceiver : BroadcastReceiver() {
    companion object {
        var screenOffTime: Long = 0L
        var screenOnTime: Long = 0L
        const val TimeInMilis = 60 * 60 * 1000
    }
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                screenOffTime = System.currentTimeMillis()
                startTimer(context)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTimer(context: Context) {
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val screenOffDuration = System.currentTimeMillis() - screenOffTime
            if (screenOffDuration >= TimeInMilis) {
                // Perform the action when screen off duration reaches 2 minutes
                val helper = DBHelper(context)
                val mobileDataList = helper.getAllMobileData()
                val message = "Mobile Phone Is Inactive For A Long Time"

                try {
                    val smsManager = SmsManager.getDefault()
                    for (mobileData in mobileDataList) {
                        val phoneNumber = mobileData.mobile
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                    }
                    Toast.makeText(context, "SMS sent successfully to all numbers", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to send SMS to some numbers", Toast.LENGTH_SHORT).show()
                }

            }
        }
        handler?.postDelayed(runnable!!, TimeInMilis.toLong())
    }

    private fun stopTimer() {
        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
    }

}
