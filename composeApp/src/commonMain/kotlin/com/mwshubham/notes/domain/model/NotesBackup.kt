package com.mwshubham.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotesBackup(
    val version: Int = 1,
    val exportedAt: Long,
    val notes: List<NoteExportEntry>
)

@Serializable
data class NoteExportEntry(
    val id: Long,
    val title: String,
    val message: String,
    val createdAt: Long,
    val updatedAt: Long
)
