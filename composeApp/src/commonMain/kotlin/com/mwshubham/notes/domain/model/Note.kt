package com.mwshubham.notes.domain.model

/**
 * Domain model representing a single note.
 * This is the canonical model used across all layers above the data layer.
 */
data class Note(
    val id: Long = 0L,
    val title: String,
    val message: String,
    val createdAt: Long,   // epoch millis — set once on creation
    val updatedAt: Long    // epoch millis — updated on every save
)
