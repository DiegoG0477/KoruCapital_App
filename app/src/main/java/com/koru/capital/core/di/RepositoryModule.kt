package com.koru.capital.core.di

import com.koru.capital.auth.data.local.DataStoreTokenStorage
import com.koru.capital.auth.data.local.TokenStorage
import com.koru.capital.auth.data.datasource.AuthApiService
import com.koru.capital.auth.data.repository.AuthRepositoryImpl
import com.koru.capital.auth.domain.repository.AuthRepository
import com.koru.capital.business.data.datasource.BusinessApiService
import com.koru.capital.business.data.repository.BusinessRepositoryImpl
import com.koru.capital.business.domain.repository.BusinessRepository
import com.koru.capital.core.data.remote.LocationApiService
import com.koru.capital.core.data.remote.StorageApiService
import com.koru.capital.core.data.repository.FileRepositoryImpl
import com.koru.capital.core.data.repository.LocationRepositoryImpl
import com.koru.capital.core.domain.repository.FileRepository
import com.koru.capital.core.domain.repository.LocationRepository
import com.koru.capital.home.data.datasource.HomeApiService
import com.koru.capital.home.data.repository.HomeRepositoryImpl
import com.koru.capital.home.domain.repository.HomeRepository
import com.koru.capital.profile.data.datasource.ProfileApiService
import com.koru.capital.profile.data.repository.ProfileRepositoryImpl
import com.koru.capital.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTokenStorage(impl: DataStoreTokenStorage): TokenStorage

    @Binds @Singleton abstract fun bindFileRepository(impl: FileRepositoryImpl): FileRepository

    companion object {
        @Provides
        @Singleton
        fun provideStorageApiService(retrofit: Retrofit): StorageApiService {
            return retrofit.create(StorageApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideBusinessRepository(apiService: BusinessApiService): BusinessRepository {
            return BusinessRepositoryImpl(apiService)
        }

        @Provides
        @Singleton
        fun provideLocationRepository(apiService: LocationApiService): LocationRepository {
            return LocationRepositoryImpl(apiService)
        }

        @Provides
        @Singleton
        fun provideHomeRepository(apiService: HomeApiService): HomeRepository {
            return HomeRepositoryImpl(apiService)
        }

        @Provides
        @Singleton
        fun provideProfileRepository(apiService: ProfileApiService): ProfileRepository {
            return ProfileRepositoryImpl(apiService)
        }

        @Provides
        @Singleton
        fun provideAuthRepository(
            apiService: AuthApiService,
            tokenStorage: TokenStorage
        ): AuthRepository {
            return AuthRepositoryImpl(apiService, tokenStorage)
        }
    }
}