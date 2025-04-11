package com.koru.capital.core.data.remote

import com.koru.capital.auth.data.local.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val path = originalRequest.url.encodedPath
        val needsAuth = !path.contains("/auth/login") &&
                !path.contains("/auth/register")

        if (!needsAuth) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenStorage.getAccessToken() }

        if (token == null) {
            println("AuthInterceptor: No token found for authenticated request to ${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        println("AuthInterceptor: Added Auth header to request to ${newRequest.url}")
        return chain.proceed(newRequest)
    }
}