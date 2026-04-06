package com.mwshubham.notes.data.preferences

import androidx.datastore.preferences.core.intPreferencesKey

internal const val PREFS_FILE = "vault_settings"
internal const val DEFAULT_TAP_THRESHOLD = 5

internal val KEY_TAP_THRESHOLD = intPreferencesKey("tap_threshold")
