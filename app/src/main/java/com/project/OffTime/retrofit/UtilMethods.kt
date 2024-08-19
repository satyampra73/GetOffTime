package com.db.md.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.project.OffTime.model.login.LoginResponse
import com.project.OffTime.utills.Constents
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UtilMethods {
    companion object {

        private fun isNetworkAvailable(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {

                val network = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            }
            return false
        }

        fun userLogin(
            context: Context,
            params: HashMap<String, String>,
            callbackResponse: CallbackResponse
        ) {
            if (isNetworkAvailable(context)) {
                try {
                    val getData =
                        RetrofitInstance.getRetrofitInstance(context)!!.create(GetData::class.java)
                    val call: Call<LoginResponse> = getData.login(params)
                    call.enqueue(object : Callback<LoginResponse?> {
                        override fun onResponse(
                            call: Call<LoginResponse?>,
                            response: Response<LoginResponse?>
                        ) {
                            val strResponse = Gson().toJson(response.body())
                            Log.d(
                                Constents.TagResponse,
                                "User Login Response Code : ${response.code()}\nand Response : $strResponse"
                            )
                            if (response.isSuccessful && response.body() != null) {
                                callbackResponse.success(
                                    Constents.TagResponse,
                                    strResponse,
                                    response.code()
                                )
                            } else {
                                callbackResponse.fail("Something went wrong")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                            callbackResponse.fail(t.message)
                            Log.d(Constents.TagResponse, t.message!!)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    callbackResponse.fail(e.message)
                    Log.d(Constents.TagResponse, e.message!!)
                }
            } else {
                Toast.makeText(
                    context,
                    "Please Check Your Internet Connection !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun signUp(
            context: Context,
            params: HashMap<String, String>,
            callbackResponse: CallbackResponse
        ) {
            if (isNetworkAvailable(context)) {
                try {
                    val getData =
                        RetrofitInstance.getRetrofitInstance(context)!!.create(GetData::class.java)
                    val call: Call<LoginResponse> = getData.signUp(params)
                    call.enqueue(object : Callback<LoginResponse?> {
                        override fun onResponse(
                            call: Call<LoginResponse?>,
                            response: Response<LoginResponse?>
                        ) {
                            val strResponse = Gson().toJson(response.body())
                            Log.d(
                                Constents.TagResponse,
                                "User Signup Response Code : ${response.code()}\nand Response : $strResponse"
                            )
                            if (response.isSuccessful && response.body() != null) {
                                callbackResponse.success(
                                    Constents.TagResponse,
                                    strResponse,
                                    response.code()
                                )
                            } else {
                                callbackResponse.fail("Something went wrong")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                            callbackResponse.fail(t.message)
                            Log.d(Constents.TagResponse, t.message!!)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    callbackResponse.fail(e.message)
                    Log.d(Constents.TagResponse, e.message!!)
                }
            } else {
                Toast.makeText(
                    context,
                    "Please Check Your Internet Connection !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun sendData(
            context: Context,
            params: HashMap<String, String>,
            callbackResponse: CallbackResponse
        ) {
            if (isNetworkAvailable(context)) {
                try {
                    val getData =
                        RetrofitInstance.getRetrofitInstance(context)!!.create(GetData::class.java)
                    val call: Call<LoginResponse> = getData.sendData(params)
                    call.enqueue(object : Callback<LoginResponse?> {
                        override fun onResponse(
                            call: Call<LoginResponse?>,
                            response: Response<LoginResponse?>
                        ) {
                            val strResponse = Gson().toJson(response.body())
                            Log.d(
                                Constents.TagResponse,
                                "Send Data Response Code : ${response.code()}\nand Response : $strResponse"
                            )
                            if (response.isSuccessful && response.body() != null) {
                                callbackResponse.success(
                                    Constents.TagResponse,
                                    strResponse,
                                    response.code()
                                )
                            } else {
                                callbackResponse.fail("Something went wrong")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                            callbackResponse.fail(t.message)
                            Log.d(Constents.TagResponse, t.message!!)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    callbackResponse.fail(e.message)
                    Log.d(Constents.TagResponse, e.message!!)
                }
            } else {
                Toast.makeText(
                    context,
                    "Please Check Your Internet Connection !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}