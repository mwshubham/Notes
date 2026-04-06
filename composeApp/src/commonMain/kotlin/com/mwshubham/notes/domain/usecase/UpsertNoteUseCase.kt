package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.repository.NoteRepository

/**
 * Inserts or updates a note.
 * If [note.id] == 0 a new note is created; otherwise the existing row is updated.
 * @return the row ID of the inserted/updated note.
 */
class UpsertNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long = repository.upsertNote(note)
}
