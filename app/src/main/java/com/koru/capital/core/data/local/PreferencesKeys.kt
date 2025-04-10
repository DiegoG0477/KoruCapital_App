package com.koru.capital.core.data.local

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val TOKEN_EXPIRES_AT = longPreferencesKey("token_expires_at") // Almacenaremos el timestamp de expiración
}