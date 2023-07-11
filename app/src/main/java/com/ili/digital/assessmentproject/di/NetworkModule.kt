package com.ili.digital.assessmentproject.di

import android.content.Context
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import com.ili.digital.assessmentproject.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {


    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Will be used for building interface API calling throughout the lifecycle of the application
     */
    @Singleton
    @Provides
    fun provideRetrofitClient(@ApplicationContext context: Context): ApiInterface {
        return RetrofitClient.createRetroClient(context).create(ApiInterface::class.java)
    }
}