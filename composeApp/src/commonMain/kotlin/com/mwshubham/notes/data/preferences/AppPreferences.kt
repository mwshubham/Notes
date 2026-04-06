package com.mwshubham.notes.data.preferences

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

internal const val PREFS_FILE = "vault_settings"
internal const val DEFAULT_TAP_THRESHOLD = 5

internal val KEY_TAP_THRESHOLD = intPreferencesKey("tap_threshold")
internal val KEY_LAST_BACKUP_TIMESTAMP = longPreferencesKey("last_backup_timestamp")
