package com.project.OffTime.utills

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.contentValuesOf
import com.db.md.retrofit.CallbackResponse
import com.db.md.retrofit.UtilMethods
import com.google.gson.Gson
import com.project.OffTime.db.DBHelper
import com.project.OffTime.model.Data
import com.project.OffTime.model.login.LoginResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScreenReceiver : BroadcastReceiver() {
    companion object {
        var screenOffTime: Long = 0L
        var screenOnTime: Long = 0L

        //       const val TimeInMilis = 60 * 60 * 1000   or 1*60000
        var TimeInMilis = 0
        var batteryPercentage = 0
    }

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val userSession = UserSession(context)
        val time = userSession.getData(Constents.MinMinutes)?.toInt() ?: 0
        TimeInMilis = time * 60 * 1000
        batteryPercentage = userSession.getData(Constents.BatterPercentage)?.toInt() ?: 0
        val currentBatteryPercentage = getBatteryPercentage(context)
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                screenOffTime = System.currentTimeMillis()
                startTimer(context)
                if (currentBatteryPercentage <= batteryPercentage) {
                    val helper = DBHelper(context)
                    val mobileDataList = helper.getAllMobileData()
                    val message = "Mobile Phone Battery Is $currentBatteryPercentage "

                    try {
                        val smsManager = SmsManager.getDefault()
                        for (mobileData in mobileDataList) {
                            val phoneNumber = mobileData.mobile
                            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                        }
                        Log.d("strData", "SMS sent successfully to all numbers")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("strData", "Failed to send SMS to some numbers")
                    }

                }
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
                sendDataToServer(context, getTodayDate(), formattedDuration)
            }
        }
    }

    private fun sendDataToServer(context: Context, date: String, duration: String) {
        val params: HashMap<String, String> = HashMap()
        params["date"] = date
        params["time"] = duration
        UtilMethods.sendData(context, params, object : CallbackResponse {
            override fun success(from: String?, message: String?, responseCode: Int) {
                Log.d(Constents.TagResponse, "response : $message")

            }

            override fun fail(from: String?) {
                Log.d(Constents.TagResponse, "fail : $from")
            }

        })

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
                val helper = DBHelper(context)
                val mobileDataList = helper.getAllMobileData()
                val message = "Mobile Phone Is Inactive For A Long Time"

                try {
                    val smsManager = SmsManager.getDefault()
                    for (mobileData in mobileDataList) {
                        val phoneNumber = mobileData.mobile
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                    }
                    Toast.makeText(
                        context,
                        "SMS sent successfully to all numbers",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "Failed to send SMS to some numbers",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun getBatteryPercentage(context: Context): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, ifilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        val batteryPct = level / scale.toFloat() * 100
        return batteryPct.toInt()
    }

}
