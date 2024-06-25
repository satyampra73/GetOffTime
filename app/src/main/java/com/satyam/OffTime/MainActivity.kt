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
import androidx.recyclerview.widget.LinearLayoutManager
import com.satyam.OffTime.adapter.StringAdapter
import com.satyam.OffTime.databinding.ActivityMainBinding
import com.satyam.OffTime.db.DBHelper

class MainActivity : AppCompatActivity(){
    private lateinit var screenReceiver: ScreenReceiver

    val REQUEST_NOTIFICATION_PERMISSION = 1

    private lateinit var dbHelper: DBHelper
    private lateinit var stringAdapter: StringAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun onDestroy() {
        super.onDestroy()
        // Unregister MainActivity as a listener when the activity is destroyed
        dbHelper.close()
    }

    // Implement the onDataChanged method from DataChangeListener interface


    override fun onResume() {

        dbHelper = DBHelper(this)


        // Initialize RecyclerView and adapter
        val list = dbHelper.getAllStrings()
        stringAdapter = StringAdapter(this, list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = stringAdapter
        stringAdapter.notifyDataSetChanged()
        super.onResume()

    }


}