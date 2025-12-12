package com.turtlpass.useraccount.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountIdRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val DATASTORE_NAME = "account_ids_datastore"
        private val ACCOUNT_ID_KEY = stringPreferencesKey("account_id")
    }

    // Create a DataStore instance tied to the application context
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    /**
     * Persist a single account ID.
     */
    suspend fun persistAccountId(accountId: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCOUNT_ID_KEY] = accountId
        }
    }

    /**
     * Retrieve the stored account ID.
     */
    fun retrieveAccountId(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[ACCOUNT_ID_KEY]
        }
    }

    /**
     * Delete only the stored account ID.
     */
    suspend fun deleteAccountId() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCOUNT_ID_KEY)
        }
    }

    /**
     * Clear all entries in the DataStore.
     */
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
