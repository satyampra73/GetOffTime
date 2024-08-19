package com.project.OffTime.activities

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.OffTime.R
import com.project.OffTime.adapter.ContactInfoAdapter
import com.project.OffTime.adapter.StringAdapter
import com.project.OffTime.databinding.ActivityMainBinding
import com.project.OffTime.db.DBHelper
import com.project.OffTime.model.EmergencyData
import com.project.OffTime.utills.Constents
import com.project.OffTime.utills.ScreenOffService
import com.project.OffTime.utills.ScreenReceiver
import com.project.OffTime.utills.UserSession
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var screenReceiver: ScreenReceiver

    val REQUEST_NOTIFICATION_PERMISSION = 1
    lateinit var dialog: Dialog
    private lateinit var dbHelper: DBHelper
    private lateinit var stringAdapter: StringAdapter
    lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 123
    lateinit var userSession : UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(Color.WHITE)
        userSession = UserSession(this)

        Log.d(Constents.TagData,"user Token :"+userSession.getData(Constents.userToken))
        binding.txtName.text=userSession.getData(Constents.userName)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(
//                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
//                    REQUEST_NOTIFICATION_PERMISSION
//                )
//            } else {
//                if (!ScreenOffService.isRunning) {
//                    startScreenService()
//                } else {
//                    Log.d("strCheck", "Ok No Need to Run Again")
//                }
//                Log.d("strCheck", "On else of top")
//            }
//        } else {
//            if (!ScreenOffService.isRunning) {
//                startScreenService()
//            }
//        }


        checkAndRequestPermissions()


        dialog = Dialog(this@MainActivity)
        binding.btnAdd.setOnClickListener {
            openDialog()
        }

        binding.btnLogOut.setOnClickListener{
          userSession.logoutUser(this@MainActivity)
        }


    }


    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        // Check SEND_SMS permission
        val smsPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(android.Manifest.permission.SEND_SMS)
        }

        // Check POST_NOTIFICATIONS permission for devices running TIRAMISU or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // All permissions are already granted
            startScreenService()
        }
    }

    private fun openDialog() {
        dialog.setContentView(R.layout.ask_for_number_view)
        dialog.window
            ?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        dialog.setCancelable(true)
        dialog.window!!.attributes.windowAnimations = R.style.animation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val etMobile = dialog.findViewById<EditText>(R.id.etEnterMobile)
        val etName = dialog.findViewById<EditText>(R.id.etEnterName)
        val etRelation = dialog.findViewById<EditText>(R.id.etRelation)
        val btnAddWallet = dialog.findViewById<AppCompatButton>(R.id.btnAddWallet)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)

        btnAddWallet.setOnClickListener(View.OnClickListener {
            if (etMobile.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please Enter Mobile No. ", Toast.LENGTH_SHORT)
                    .show()
            } else if (etMobile.text.toString().length != 10) {
                Toast.makeText(
                    this@MainActivity,
                    "Please Enter Valid Mobile No. ",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (etName.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please Enter Name ", Toast.LENGTH_SHORT)
                    .show()
            } else if (etRelation.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please Enter Relation ", Toast.LENGTH_SHORT)
                    .show()
            } else {

                dialog.dismiss()
                dbHelper.insertData(
                    etMobile.text.toString(),
                    etName.text.toString(),
                    etRelation.text.toString()
                )
                Toast.makeText(this@MainActivity, "Details Added Successfully.", Toast.LENGTH_SHORT)
                    .show()
                getData()
            }
        })

        btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

        dialog.show()
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
                Log.d("strCheck", "Notification permission granted")
                startScreenService()  // Start the service when permission is granted
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Notification permission required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Unregister MainActivity as a listener when the activity is destroyed
        dbHelper.close()
    }

    // Implement the onDataChanged method from DataChangeListener interface


    override fun onResume() {

        getData()
        // Initialize RecyclerView and adapter
//        val dataList = dbHelper.getAllStrings()
//        stringAdapter = StringAdapter(this, dataList)
//
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = stringAdapter
//        stringAdapter.notifyDataSetChanged()


        super.onResume()

    }

    private fun getData() {
        dbHelper = DBHelper(this)

        try {
            val emergencyList: ArrayList<EmergencyData> = dbHelper.getAllMobileData()

//            if (mobile != null && mobile.isNotEmpty()) {
//                binding.txtMobile.text = mobile
//                binding.ltMobile.visibility = View.VISIBLE
//                binding.btnAdd.visibility = View.GONE
//            } else {
//                binding.ltMobile.visibility = View.GONE
//                binding.btnAdd.visibility = View.VISIBLE
//            }
//            for (data in emergencyList) {
//                Log.d("strData", data.toString())
//            }

            val adapter = ContactInfoAdapter(this, emergencyList)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error retrieving mobile number: ${e.message}", Toast.LENGTH_LONG)
                .show()
            Log.d("strData", "Error retrieving mobile number: ${e.message}")
        }

    }


}