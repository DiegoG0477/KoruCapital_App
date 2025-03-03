package com.koru.capital.di

import com.koru.capital.business.data.remote.BusinessApiService
import com.koru.capital.business.data.repository.BusinessRepositoryImpl
import com.koru.capital.business.domain.BusinessRepository
import com.koru.capital.core.data.remote.LocationApiService
import com.koru.capital.core.data.repository.LocationRepositoryImpl
import com.koru.capital.core.domain.repository.LocationRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBusinessRepository(
        apiService: BusinessApiService
    ): BusinessRepository {
        return BusinessRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        apiService: LocationApiService
    ): LocationRepository {
        return LocationRepositoryImpl(apiService)
    }
}