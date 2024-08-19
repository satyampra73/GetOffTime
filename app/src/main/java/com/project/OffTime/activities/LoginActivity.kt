package com.project.OffTime.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.db.md.retrofit.CallbackResponse
import com.db.md.retrofit.UtilMethods
import com.google.gson.Gson
import com.project.OffTime.R
import com.project.OffTime.databinding.ActivityLoginBinding
import com.project.OffTime.model.login.LoginResponse
import com.project.OffTime.utills.CustomProgressBar
import com.project.OffTime.utills.UserSession

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    lateinit var userSession:UserSession
    val progressBar = CustomProgressBar()
    val params = HashMap<String,String>()
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
                params["mobile"] = binding.etMobileNo.text.toString().trim()
                params["password"] = binding.etPassword.text.toString().trim()
                userLogin(this@LoginActivity)
            }
        }
        binding.txtSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun userLogin (context: Context){
        progressBar.showProgress(context)
        UtilMethods.userLogin(context,params,object : CallbackResponse{
            override fun success(from: String?, message: String?, responseCode: Int) {
                progressBar.hideProgress()
                val response = Gson().fromJson(message, LoginResponse::class.java)
                if (response.status) {
                    userSession.createUserLoginSession(response.token, response.data.name)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else{
                    Toast.makeText(context,response.msg,Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String?) {
                progressBar.hideProgress()
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })

    }
}