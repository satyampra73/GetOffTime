package com.db.md.retrofit


import com.project.OffTime.model.login.LoginResponse
import com.project.OffTime.utills.ApiUrls
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface GetData {

    @POST(ApiUrls.Login)
    @FormUrlEncoded
    fun login(
        @FieldMap params: HashMap<String, String>
    ): Call<LoginResponse>

    @POST(ApiUrls.SignUp)
    @FormUrlEncoded
    fun signUp(
        @FieldMap params: HashMap<String, String>
    ): Call<LoginResponse>

    @POST(ApiUrls.SendData)
    @FormUrlEncoded
    fun sendData(
        @FieldMap params: HashMap<String, String>
    ): Call<LoginResponse>

}