// capital/core/di/NetworkModule.kt
package com.koru.capital.core.di

import com.koru.capital.auth.data.datasource.AuthApiService
import com.koru.capital.business.data.datasource.BusinessApiService
import com.koru.capital.core.data.remote.LocationApiService
import com.koru.capital.home.data.datasource.HomeApiService
import com.koru.capital.profile.data.datasource.ProfileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient // <-- IMPORTAR OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // <-- IMPORTAR Logging
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.koru.capital.core.data.remote.AuthInterceptor // <-- IMPORTAR AuthInterceptor
import java.util.concurrent.TimeUnit // <-- IMPORTAR TimeUnit (opcional para timeouts)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Proveer el Interceptor de Logging (Opcional pero recomendado para debug)
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Nivel BODY es muy verboso para producción, considera cambiarlo
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // AuthInterceptor será proveído automáticamente por Hilt vía @Inject constructor

    // Proveer OkHttpClient configurado con los interceptors
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor, // Hilt inyecta AuthInterceptor (que a su vez tiene TokenStorage)
        loggingInterceptor: HttpLoggingInterceptor // Hilt inyecta el LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // <-- Añadir interceptor de Auth
            .addInterceptor(loggingInterceptor) // <-- Añadir interceptor de Logging
            // Configurar timeouts (opcional pero recomendado)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Proveer Retrofit usando el OkHttpClient configurado
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // <-- Inyectar OkHttpClient configurado
        // ¡ASEGÚRATE QUE ESTA URL BASE SEA CORRECTA Y ACCESIBLE DESDE TU DISPOSITIVO/EMULADOR!
        // Si usas emulador y el backend corre en localhost: usa 10.0.2.2
        // Si usas dispositivo físico: usa la IP de tu máquina en la red local (ej. 192.168.x.x)
        // const val BASE_URL = "http://10.0.2.2:8080" // Ejemplo para Emulador Android
        val BASE_URL = "http://192.168.43.248:8080" // Tu IP local (¡Asegúrate que sea correcta!)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- USAR el cliente configurado con interceptors
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Proveedores de Servicios API (sin cambios, usan el Retrofit configurado) ---
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

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}