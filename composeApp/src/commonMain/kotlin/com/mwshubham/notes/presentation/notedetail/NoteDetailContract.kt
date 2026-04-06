package com.mwshubham.notes.presentation.notedetail

import com.mwshubham.notes.domain.model.Note

data class NoteDetailState(
    val id: Long? = null,
    val title: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false
)

sealed interface NoteDetailIntent {
    data class LoadNote(val id: Long) : NoteDetailIntent
    data class TitleChanged(val value: String) : NoteDetailIntent
    data class MessageChanged(val value: String) : NoteDetailIntent
    data object SaveClicked : NoteDetailIntent
    data object DeleteClicked : NoteDetailIntent
}

sealed interface NoteDetailEffect {
    data object NavigateBack : NoteDetailEffect
    data class ShowSnackbar(val message: String) : NoteDetailEffect
}
