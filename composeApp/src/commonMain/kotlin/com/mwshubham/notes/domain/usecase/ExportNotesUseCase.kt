package com.mwshubham.notes.domain.usecase

import com.mwshubham.notes.domain.model.NoteExportEntry
import com.mwshubham.notes.domain.model.NotesBackup
import com.mwshubham.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Clock

private val exportJson = Json { prettyPrint = true }

/** Serialises all notes to a JSON backup string. */
class ExportNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(): String {
        val notes = repository.getAllNotes().first()
        val backup = NotesBackup(
            exportedAt = Clock.System.now().toEpochMilliseconds(),
            notes = notes.map { note ->
                NoteExportEntry(
                    id = note.id,
                    title = note.title,
                    message = note.message,
                    createdAt = note.createdAt,
                    updatedAt = note.updatedAt
                )
            }
        )
        return exportJson.encodeToString(backup)
    }
}
