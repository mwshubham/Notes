package com.mwshubham.notes.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var appContext: Context

/** Called once from [MainActivity] before any database access. */
fun initDatabaseContext(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<NotesDatabase> {
    val dbFile = appContext.getDatabasePath("notes_vault.db")
    return Room.databaseBuilder<NotesDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
