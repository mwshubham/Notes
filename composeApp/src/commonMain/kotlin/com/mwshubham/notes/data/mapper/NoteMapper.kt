package com.mwshubham.notes.data.mapper

import com.mwshubham.notes.data.local.entity.NoteEntity
import com.mwshubham.notes.domain.model.Note

/** Maps a [NoteEntity] (data layer) to a [Note] (domain layer). */
fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    message = message,
    createdAt = createdAt,
    updatedAt = updatedAt
)

/** Maps a [Note] (domain layer) to a [NoteEntity] (data layer). */
fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    message = message,
    createdAt = createdAt,
    updatedAt = updatedAt
)
