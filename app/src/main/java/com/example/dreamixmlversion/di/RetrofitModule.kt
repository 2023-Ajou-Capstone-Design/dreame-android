package com.example.dreamixmlversion.di

import com.example.dreamixmlversion.data.api.DreameApi
import com.example.dreamixmlversion.data.api.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Url.MOCKY_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): DreameApi {
        return retrofit.create(DreameApi::class.java)
    }
}