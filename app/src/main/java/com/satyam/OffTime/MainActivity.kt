package com.satyam.OffTime

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var screenReceiver: ScreenReceiver

    val REQUEST_NOTIFICATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenReceiver = ScreenReceiver()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(screenReceiver, filter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            } else {
                startScreenService()
                Log.d("strCheck", "On else of top")
            }
        }
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(screenReceiver)
//    }

    private fun startScreenService() {
        val intent = Intent(applicationContext, ScreenOffService::class.java)
        intent.setAction(Constents.ActionStartScreenService)
        startService(intent)
        Toast.makeText(this@MainActivity, "Service Started", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScreenService()
                Log.d("strCheck", "inside if on onRequestPermissionResult")
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Notification permission required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isScreenOffServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        if (activityManager != null) {
            val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
            for (service in runningServices) {
                if (ScreenOffService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
        }
        return false
    }


}