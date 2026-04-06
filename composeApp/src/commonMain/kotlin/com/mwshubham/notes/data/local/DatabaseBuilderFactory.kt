package com.mwshubham.notes.data.local

import androidx.room.RoomDatabase

/**
 * Provides a platform-specific [RoomDatabase.Builder] for [NotesDatabase].
 * Each platform implements how to locate the database file.
 */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<NotesDatabase>
