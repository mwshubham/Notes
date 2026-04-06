package com.mwshubham.notes.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwshubham.notes.core.logger.AppLogger
import com.mwshubham.notes.domain.usecase.GetAllNotesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "NoteListViewModel"

class NoteListViewModel(
    private val getAllNotes: GetAllNotesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    private val _effect = Channel<NoteListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeNotes()
    }

    fun onIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.AddNoteClicked -> {
                AppLogger.i(TAG, "Add note clicked")
                viewModelScope.launch { _effect.send(NoteListEffect.NavigateToAddNote) }
            }
            is NoteListIntent.NoteClicked -> {
                AppLogger.i(TAG, "Note clicked id=${intent.id}")
                viewModelScope.launch { _effect.send(NoteListEffect.NavigateToEditNote(intent.id)) }
            }
            is NoteListIntent.SettingsClicked -> {
                AppLogger.i(TAG, "Settings clicked")
                viewModelScope.launch { _effect.send(NoteListEffect.NavigateToSettings) }
            }
        }
    }

    private fun observeNotes() {
        viewModelScope.launch {
            getAllNotes()
                .onStart {
                    AppLogger.d(TAG, "Starting notes observation")
                    _state.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    AppLogger.e(TAG, "Error loading notes", e)
                    _state.update { it.copy(isLoading = false) }
                }
                .collect { notes ->
                    AppLogger.d(TAG, "Notes updated — count=${notes.size}")
                    _state.update { it.copy(notes = notes, isLoading = false) }
                }
        }
    }
}
