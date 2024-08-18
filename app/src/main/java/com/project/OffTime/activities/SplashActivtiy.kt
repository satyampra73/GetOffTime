package com.project.OffTime.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.OffTime.R
import com.project.OffTime.utills.UserSession

class SplashActivtiy : AppCompatActivity() {
    lateinit var userSession: UserSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activtiy)
        userSession = UserSession(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (userSession.isUserLoggedIn) {
                val intent = Intent(this@SplashActivtiy, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this@SplashActivtiy, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 1500)
    }
}