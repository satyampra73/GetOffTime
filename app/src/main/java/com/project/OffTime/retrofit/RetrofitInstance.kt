package com.db.md.retrofit

import android.content.Context
import com.project.OffTime.utills.ApiUrls
import com.project.OffTime.utills.Constents
import com.project.OffTime.utills.UserSession
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object{
        private lateinit var userSession: UserSession
        private lateinit var token: String
        fun getRetrofitInstance(context: Context?): Retrofit? {
            userSession= UserSession(context)
            token= userSession.getData(Constents.userToken).toString()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(Interceptor { chain -> //                        assert token != null;
                val newRequest = chain.request().newBuilder()
                    .addHeader(Constents.ApiKeyName,"Bearer $token")
                    .build()
                chain.proceed(newRequest)
            })
                .addInterceptor(loggingInterceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()
            val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl(ApiUrls.BaseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
            return builder.build()
        }
    }
}