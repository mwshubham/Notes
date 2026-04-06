package com.mwshubham.notes.domain.repository

import com.mwshubham.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Contract for note persistence. Implementation lives in the data layer.
 * Only domain models cross this boundary — never entities.
 */
interface NoteRepository {
    /** Reactive stream of all notes, ordered by last updated descending. */
    fun getAllNotes(): Flow<List<Note>>

    /** Returns a single note by [id], or null if not found. */
    suspend fun getNoteById(id: Long): Note?

    /**
     * Inserts a new note or updates an existing one (if [note.id] != 0).
     * @return the inserted/updated row ID.
     */
    suspend fun upsertNote(note: Note): Long

    /** Permanently deletes [note] from storage. */
    suspend fun deleteNote(note: Note)
}
