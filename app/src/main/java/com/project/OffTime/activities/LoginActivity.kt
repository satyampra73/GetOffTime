package com.project.OffTime.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.OffTime.R
import com.project.OffTime.databinding.ActivityLoginBinding
import com.project.OffTime.utills.UserSession

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    lateinit var userSession:UserSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userSession=UserSession(this@LoginActivity)
        binding.btnLogin.setOnClickListener{
            if (binding.etMobileNo.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etMobileNo.text.toString().trim().length<10){
                Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etPassword.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            }
            else{
                userSession.createUserLoginSession("token","UserName")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding.txtSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}