package com.koru.capital.auth.domain.usecase

import com.koru.capital.auth.data.local.TokenStorage // Usar TokenStorage
import io.jsonwebtoken.Claims // Necesitas dependencia de jjwt: 'io.jsonwebtoken:jjwt-api', 'io.jsonwebtoken:jjwt-impl', 'io.jsonwebtoken:jjwt-jackson'
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys // Para decodificar sin validar firma (¡Cuidado!)
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    /**
     * Obtiene el ID del usuario actual decodificando el token JWT almacenado.
     * ¡ADVERTENCIA! Esta implementación decodifica SIN verificar la firma del token,
     * lo cual es aceptable si confías en que el token almacenado es válido,
     * pero NO es seguro si el token podría ser manipulado.
     * Una implementación más segura verificaría la firma con la clave secreta.
     * @return El ID del usuario como String, o null si no hay token o no se puede decodificar.
     */
    suspend operator fun invoke(): String? {
        val token = tokenStorage.getAccessToken() ?: return null
        return try {
            // Decodificar SIN verificar la firma (solo para obtener el ID del payload)
            // Esto requiere conocer la estructura del payload ({ "id": "..." })
            // Separar el token en partes: header.payload.signature
            val withoutSignature = token.substringBeforeLast('.')
            // Decodificar el payload (puede necesitar un parser JSON si no usas libreria jjwt completa)
            // Con jjwt-api (simplificado, puede fallar si el token usa compresión etc):
            // val claims = Jwts.parser().parseClaimsJwt(withoutSignature).body as Claims // <- Esto necesita más configuración

            // Alternativa simple (¡MENOS SEGURA, NO VERIFICA FIRMA!): Decodificar Base64
            val parts = token.split('.')
            if (parts.size < 2) return null // Token inválido
            val payloadBytes = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
            val payloadJson = String(payloadBytes, Charsets.UTF_8)
            // Parsear JSON manualmente o con Gson/Moshi
            val payloadMap = com.google.gson.Gson().fromJson(payloadJson, Map::class.java) as Map<String, Any?>
            payloadMap["id"]?.toString() // Obtener el ID del mapa

        } catch (e: Exception) {
            println("Error decoding JWT payload: ${e.message}")
            null
        }
    }
}