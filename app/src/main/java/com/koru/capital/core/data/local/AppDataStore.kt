package com.koru.capital.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Define el DataStore a nivel de Context (singleton por nombre)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "koru_capital_settings")