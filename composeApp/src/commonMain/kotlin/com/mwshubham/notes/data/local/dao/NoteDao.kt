package com.mwshubham.notes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mwshubham.notes.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    /**
     * Inserts a new note or replaces an existing one by primary key conflict.
     * @return the row ID of the inserted/updated row.
     */
    @Upsert
    suspend fun upsert(note: NoteEntity): Long

    /** Deletes a note by matching its primary key. */
    @Delete
    suspend fun delete(note: NoteEntity)

    /** Reactive stream — emits a new list whenever the notes table changes. */
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    /** Returns a single note by ID, or null if not found. */
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?
}
