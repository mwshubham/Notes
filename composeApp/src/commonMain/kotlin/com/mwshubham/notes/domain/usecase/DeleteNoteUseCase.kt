package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.repository.NoteRepository

/** Permanently deletes the given note from storage. */
class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}
