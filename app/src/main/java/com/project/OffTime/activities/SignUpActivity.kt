package com.project.OffTime.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.OffTime.R
import com.project.OffTime.databinding.ActivitySignUpBinding
import com.project.OffTime.utills.UserSession

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var userSession: UserSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userSession=UserSession(this)
        binding.btnSignUp.setOnClickListener{
            if (binding.etName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etMobileNo.text.toString().trim().length<10){
                Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etMobileNo.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your Mobile No.", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etCreatePassword.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please Crate your Password", Toast.LENGTH_SHORT).show()
            }
            else{
                userSession.createUserLoginSession("token","UserName")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }

        binding.txtLogin.setOnClickListener {
            finish()
        }

    }
}