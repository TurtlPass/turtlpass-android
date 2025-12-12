package com.turtlpass.biometric.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("encrypted_pin")

@Singleton
class PinRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun isPinPresent(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[ENCRYPTED_PIN] != null && prefs[INITIALIZATION_VECTOR] != null
    }

    suspend fun persistPin(pinBase64: String) {
        context.dataStore.edit { prefs ->
            prefs[ENCRYPTED_PIN] = pinBase64
        }
    }

    suspend fun persistInitializationVector(ivBase64: String) {
        context.dataStore.edit { prefs ->
            prefs[INITIALIZATION_VECTOR] = ivBase64
        }
    }

    suspend fun retrievePin(): String? =
        context.dataStore.data.map { it[ENCRYPTED_PIN] }.firstOrNull()

    suspend fun retrieveInitializationVector(): String? =
        context.dataStore.data.map { it[INITIALIZATION_VECTOR] }.firstOrNull()

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    companion object {
        private val ENCRYPTED_PIN = stringPreferencesKey("encrypted_pin")
        private val INITIALIZATION_VECTOR = stringPreferencesKey("initialization_vector")
    }
}
