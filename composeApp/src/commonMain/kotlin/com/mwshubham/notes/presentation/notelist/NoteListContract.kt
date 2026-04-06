package com.mwshubham.notes.presentation.notelist

import com.mwshubham.notes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface NoteListIntent {
    data object AddNoteClicked : NoteListIntent
    data class NoteClicked(val id: Long) : NoteListIntent
    data object SettingsClicked : NoteListIntent
}

sealed interface NoteListEffect {
    data object NavigateToAddNote : NoteListEffect
    data class NavigateToEditNote(val id: Long) : NoteListEffect
    data object NavigateToSettings : NoteListEffect
}
