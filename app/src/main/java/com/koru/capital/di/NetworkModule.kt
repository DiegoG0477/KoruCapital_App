package com.koru.capital.di

import com.koru.capital.business.data.remote.BusinessApiService
import com.koru.capital.core.data.remote.LocationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.tuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBusinessApiService(retrofit: Retrofit): BusinessApiService {
        return retrofit.create(BusinessApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationApiService(retrofit: Retrofit): LocationApiService {
        return retrofit.create(LocationApiService::class.java)
    }
}