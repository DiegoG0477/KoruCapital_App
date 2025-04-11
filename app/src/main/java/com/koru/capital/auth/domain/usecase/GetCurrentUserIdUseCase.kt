package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.data.local.TokenStorage
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    suspend operator fun invoke(): String? {
        val token = tokenStorage.getAccessToken() ?: return null
        return try {
            val withoutSignature = token.substringBeforeLast('.')

            val parts = token.split('.')
            if (parts.size < 2) return null
            val payloadBytes = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
            val payloadJson = String(payloadBytes, Charsets.UTF_8)
            val payloadMap = com.google.gson.Gson().fromJson(payloadJson, Map::class.java) as Map<String, Any?>
            payloadMap["id"]?.toString()

        } catch (e: Exception) {
            println("Error decoding JWT payload: ${e.message}")
            null
        }
    }
}