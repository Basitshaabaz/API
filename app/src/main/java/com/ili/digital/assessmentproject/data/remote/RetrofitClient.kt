package com.ili.digital.assessmentproject.data.remote

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.ili.digital.assessmentproject.util.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {

        fun createRetroClient(context: Context): Retrofit {

            val gson = GsonBuilder().create()
            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.retryOnConnectionFailure(true)
            httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            httpClientBuilder.readTimeout(40, TimeUnit.SECONDS)
            httpClientBuilder.writeTimeout(40, TimeUnit.SECONDS)
            httpClientBuilder.addInterceptor(getLoggingIntercept()!!)

            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            httpClientBuilder.cookieJar(JavaNetCookieJar(cookieManager))
                .build()

            val httpClient = httpClientBuilder.build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()
        }

        private fun getLoggingIntercept(): Interceptor? {
            val logging = HttpLoggingInterceptor { message: String ->
                // used if for intercepting the response of the API which are used trough out the application
            Log.e("Network",message)
            }
            return logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    }
}