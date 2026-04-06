package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.repository.NoteRepository

/** Fetches a single note by its ID. Returns null if not found. */
class GetNoteByIdUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long): Note? = repository.getNoteById(id)
}
