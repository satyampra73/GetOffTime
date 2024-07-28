package com.project.OffTime

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.OffTime.databinding.ActivityAllContactsListBinding

class AllContactsListActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllContactsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAllContactsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}