package com.koru.capital.auth.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.koru.capital.auth.domain.model.AuthToken
import com.koru.capital.core.data.local.PreferencesKeys
import com.koru.capital.core.data.local.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreTokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenStorage {

    private val dataStore = context.dataStore

    override suspend fun saveToken(token: AuthToken) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token.accessToken
            token.refreshToken?.let { preferences[PreferencesKeys.REFRESH_TOKEN] = it }
                ?: preferences.remove(PreferencesKeys.REFRESH_TOKEN)

            val expiresInMillis = token.expiresIn?.let { System.currentTimeMillis() + (it * 1000) }
            expiresInMillis?.let { preferences[PreferencesKeys.TOKEN_EXPIRES_AT] = it }
                ?: preferences.remove(PreferencesKeys.TOKEN_EXPIRES_AT)
        }
    }

    override suspend fun getToken(): AuthToken? {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val accessToken = preferences[PreferencesKeys.ACCESS_TOKEN]
                val refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN]
                val expiresAt = preferences[PreferencesKeys.TOKEN_EXPIRES_AT]

                if (accessToken != null && (expiresAt == null || expiresAt > System.currentTimeMillis())) {
                    AuthToken(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresIn = expiresAt?.let { (it - System.currentTimeMillis()) / 1000 }?.takeIf { it > 0 }
                    )
                } else {
                    null
                }
            }.first()
    }

    override suspend fun getAccessToken(): String? {
        val token = getToken()
        return token?.accessToken
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
            preferences.remove(PreferencesKeys.REFRESH_TOKEN)
            preferences.remove(PreferencesKeys.TOKEN_EXPIRES_AT)
        }
    }
}