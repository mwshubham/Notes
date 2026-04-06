package com.mwshubham.notes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private lateinit var appContext: Context

fun initDataStoreContext(context: Context) {
    appContext = context.applicationContext
}

actual fun createDataStore(): DataStore<Preferences> {
    val path = appContext.filesDir.resolve("$PREFS_FILE.preferences_pb").absolutePath
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() }
    )
}
