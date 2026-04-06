package com.mwshubham.notes.presentation.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwshubham.notes.core.logger.AppLogger
import com.mwshubham.notes.domain.model.Note
import com.mwshubham.notes.domain.usecase.DeleteNoteUseCase
import com.mwshubham.notes.domain.usecase.GetNoteByIdUseCase
import com.mwshubham.notes.domain.usecase.UpsertNoteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

private const val TAG = "NoteDetailViewModel"

class NoteDetailViewModel(
    private val initialNoteId: Long?,
    private val getNoteById: GetNoteByIdUseCase,
    private val upsertNote: UpsertNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState(id = initialNoteId))
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    private val _effect = Channel<NoteDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        initialNoteId?.let { onIntent(NoteDetailIntent.LoadNote(it)) }
    }

    fun onIntent(intent: NoteDetailIntent) {
        when (intent) {
            is NoteDetailIntent.LoadNote -> loadNote(intent.id)
            is NoteDetailIntent.TitleChanged -> _state.update { it.copy(title = intent.value) }
            is NoteDetailIntent.MessageChanged -> _state.update { it.copy(message = intent.value) }
            is NoteDetailIntent.SaveClicked -> save()
            is NoteDetailIntent.DeleteClicked -> delete()
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            AppLogger.d(TAG, "Loading note id=$id")
            _state.update { it.copy(isLoading = true) }
            val note = getNoteById(id)
            if (note != null) {
                AppLogger.d(TAG, "Note loaded: title='${note.title}'")
                _state.update {
                    it.copy(
                        title = note.title,
                        message = note.message,
                        isLoading = false
                    )
                }
            } else {
                AppLogger.w(TAG, "Note id=$id not found")
                _state.update { it.copy(isLoading = false) }
                _effect.send(NoteDetailEffect.ShowSnackbar("Note not found"))
            }
        }
    }

    private fun save() {
        val current = _state.value
        if (current.title.isBlank()) {
            viewModelScope.launch {
                AppLogger.w(TAG, "Save attempted with blank title")
                _effect.send(NoteDetailEffect.ShowSnackbar("Title cannot be empty"))
            }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val now = Clock.System.now().toEpochMilliseconds()
            val noteToSave = Note(
                id = current.id ?: 0L,
                title = current.title.trim(),
                message = current.message.trim(),
                createdAt = if (current.id == null) now else 0L, // preserved on update via DB
                updatedAt = now
            )
            runCatching { upsertNote(noteToSave) }
                .onSuccess { rowId ->
                    AppLogger.i(TAG, "Note saved — rowId=$rowId")
                    _state.update { it.copy(isSaving = false) }
                    _effect.send(NoteDetailEffect.NavigateBack)
                }
                .onFailure { e ->
                    AppLogger.e(TAG, "Save failed", e)
                    _state.update { it.copy(isSaving = false) }
                    _effect.send(NoteDetailEffect.ShowSnackbar("Failed to save note"))
                }
        }
    }

    private fun delete() {
        val current = _state.value
        val id = current.id ?: return  // can't delete an unsaved note
        viewModelScope.launch {
            AppLogger.w(TAG, "Deleting note id=$id")
            val note = getNoteById(id) ?: return@launch
            runCatching { deleteNote(note) }
                .onSuccess {
                    AppLogger.i(TAG, "Note deleted id=$id")
                    _effect.send(NoteDetailEffect.NavigateBack)
                }
                .onFailure { e ->
                    AppLogger.e(TAG, "Delete failed", e)
                    _effect.send(NoteDetailEffect.ShowSnackbar("Failed to delete note"))
                }
        }
    }
}
