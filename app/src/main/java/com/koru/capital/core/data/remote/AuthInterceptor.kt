// capital/core/data/remote/AuthInterceptor.kt
package com.koru.capital.core.data.remote

import com.koru.capital.auth.data.local.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // <-- AÑADIDO: Asegurar que sea singleton
class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Determinar si esta petición necesita autorización (evitar añadir a login/register)
        val path = originalRequest.url.encodedPath
        // Excluir login, registro, y potencialmente endpoints públicos como locations
        val needsAuth = !path.contains("/auth/login") &&
                !path.contains("/auth/register")
        // && !path.startsWith("/locations") // Descomentar si locations es público

        if (!needsAuth) {
            // Si no necesita auth, continuar con la petición original
            return chain.proceed(originalRequest)
        }

        // Obtener token (usar runBlocking es una simplificación aquí; idealmente,
        // la obtención/refresco del token sería más robusta en un escenario real)
        // DataStoreTokenStorage.getAccessToken() es suspend, por eso runBlocking.
        val token = runBlocking { tokenStorage.getAccessToken() }

        // Si no hay token, proceder sin añadir encabezado (la API devolverá 401 si era necesario)
        if (token == null) {
            println("AuthInterceptor: No token found for authenticated request to ${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        // Construir nueva petición con el encabezado Authorization
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        println("AuthInterceptor: Added Auth header to request to ${newRequest.url}")
        return chain.proceed(newRequest)
    }
}