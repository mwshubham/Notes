package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

/** Returns a reactive stream of all notes, ordered by most recently updated. */
class GetAllNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}
