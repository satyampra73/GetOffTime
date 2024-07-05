package com.satyam.OffTime

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.satyam.OffTime.adapter.StringAdapter
import com.satyam.OffTime.databinding.ActivityMainBinding
import com.satyam.OffTime.db.DBHelper


class MainActivity : AppCompatActivity() {
    private lateinit var screenReceiver: ScreenReceiver

    val REQUEST_NOTIFICATION_PERMISSION = 1
    lateinit var dialog: Dialog
    private lateinit var dbHelper: DBHelper
    private lateinit var stringAdapter: StringAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            } else {
                if (!ScreenOffService.isRunning) {
                    startScreenService()
                } else {
                    Log.d("strCheck", "Ok No Need to Run Again")
                }
                Log.d("strCheck", "On else of top")
            }
        } else {
            if (!ScreenOffService.isRunning) {
                startScreenService()
            }
        }


        dialog = Dialog(this@MainActivity)
        binding.btnAdd.setOnClickListener {
            openDialog()
        }

        binding.imgEdit.setOnClickListener {
            openDialog()
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

        val etEnterAmount = dialog.findViewById<EditText>(R.id.etEnterAmount)
        val btnAddWallet = dialog.findViewById<AppCompatButton>(R.id.btnAddWallet)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)

        btnAddWallet.setOnClickListener(View.OnClickListener {
            if (etEnterAmount.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please Enter Mobile No. ", Toast.LENGTH_SHORT)
                    .show()
            } else {

                dialog.dismiss()
                binding.txtMobile.text = etEnterAmount.text.toString()
                binding.ltMobile.visibility = View.VISIBLE
                binding.btnAdd.visibility = View.GONE
                dbHelper.insertOrUpdateMobile(etEnterAmount.text.toString())
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

        dbHelper = DBHelper(this)

        val dbHelper = DBHelper(this)
        try {
            val mobile = dbHelper.getMobile()
            if (mobile != null && mobile.isNotEmpty()) {
                binding.txtMobile.text = mobile
                binding.ltMobile.visibility = View.VISIBLE
                binding.btnAdd.visibility = View.GONE
            } else {
                binding.ltMobile.visibility = View.GONE
                binding.btnAdd.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error retrieving mobile number: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Initialize RecyclerView and adapter
        val dataList = dbHelper.getAllStrings()
        stringAdapter = StringAdapter(this, dataList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = stringAdapter
        stringAdapter.notifyDataSetChanged()


        super.onResume()

    }


}