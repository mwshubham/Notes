package com.mwshubham.notes.data.repository

import com.mwshubham.notes.core.logger.AppLogger
import com.mwshubham.notes.data.local.dao.NoteDao
import com.mwshubham.notes.data.mapper.toDomain
import com.mwshubham.notes.data.mapper.toEntity
import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "NoteRepositoryImpl"

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        AppLogger.d(TAG, "Subscribing to all notes stream")
        return dao.getAllNotes().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getNoteById(id: Long): Note? {
        AppLogger.d(TAG, "Fetching note id=$id")
        return dao.getNoteById(id)?.toDomain()
    }

    override suspend fun upsertNote(note: Note): Long {
        val isNew = note.id == 0L
        AppLogger.i(TAG, "${if (isNew) "Creating" else "Updating"} note: title='${note.title}'")
        return dao.upsert(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        AppLogger.w(TAG, "Deleting note id=${note.id} title='${note.title}'")
        dao.delete(note.toEntity())
    }
}
