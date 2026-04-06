package com.mwshubham.notes.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val tapThreshold: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_TAP_THRESHOLD] ?: DEFAULT_TAP_THRESHOLD
    }

    override suspend fun setTapThreshold(value: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_TAP_THRESHOLD] = value.coerceIn(2, 8)
        }
    }

    override val lastBackupTimestamp: Flow<Long?> = dataStore.data.map { prefs ->
        prefs[KEY_LAST_BACKUP_TIMESTAMP]
    }

    override suspend fun setLastBackupTimestamp(ts: Long) {
        dataStore.edit { prefs ->
            prefs[KEY_LAST_BACKUP_TIMESTAMP] = ts
        }
    }
}
