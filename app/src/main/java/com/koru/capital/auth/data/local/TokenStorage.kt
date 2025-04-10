package com.koru.capital.auth.data.local

import com.koru.capital.auth.domain.model.AuthToken

interface TokenStorage {
    suspend fun saveToken(token: AuthToken)
    suspend fun getToken(): AuthToken?
    suspend fun clearToken()
    suspend fun getAccessToken(): String?
}