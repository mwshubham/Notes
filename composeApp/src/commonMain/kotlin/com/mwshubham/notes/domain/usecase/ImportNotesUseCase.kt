package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.model.NotesBackup
import com.mwshubham.notes.domain.repository.NoteRepository
import kotlinx.serialization.json.Json

private val importJson = Json { ignoreUnknownKeys = true }

/**
 * Imports notes from a JSON backup string.
 * Notes whose ID already exists locally are skipped (local copy wins).
 * @return the count of notes actually imported.
 */
class ImportNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(content: String): Int {
        val backup = importJson.decodeFromString<NotesBackup>(content)
        var imported = 0
        for (entry in backup.notes) {
            if (repository.getNoteById(entry.id) == null) {
                repository.upsertNote(
                    Note(
                        id = entry.id,
                        title = entry.title,
                        message = entry.message,
                        createdAt = entry.createdAt,
                        updatedAt = entry.updatedAt
                    )
                )
                imported++
            }
        }
        return imported
    }
}
