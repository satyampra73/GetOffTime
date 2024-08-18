package com.project.OffTime.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.OffTime.databinding.ActivityAllContactsListBinding

class AllContactsListActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllContactsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAllContactsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}