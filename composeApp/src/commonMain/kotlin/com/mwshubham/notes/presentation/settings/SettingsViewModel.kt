package com.mwshubham.notes.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwshubham.notes.data.preferences.SettingsRepository
import com.mwshubham.notes.domain.usecase.ExportNotesUseCase
import com.mwshubham.notes.domain.usecase.ImportNotesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val exportNotes: ExportNotesUseCase,
    private val importNotes: ImportNotesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _effect = Channel<SettingsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            settingsRepository.tapThreshold.collect { threshold ->
                _state.update { it.copy(tapThreshold = threshold) }
            }
        }
        viewModelScope.launch {
            settingsRepository.lastBackupTimestamp.collect { ts ->
                _state.update { it.copy(lastBackupLabel = ts?.toFormattedDate()) }
            }
        }
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SetTapThreshold -> viewModelScope.launch {
                settingsRepository.setTapThreshold(intent.value)
            }
            SettingsIntent.ExportClicked -> viewModelScope.launch {
                try {
                    val content = exportNotes()
                    val fileName = "vault_backup_${Clock.System.now().toEpochMilliseconds()}.json"
                    _effect.send(SettingsEffect.LaunchExport(fileName, content))
                } catch (e: Exception) {
                    _effect.send(SettingsEffect.ShowMessage("Export failed: ${e.message}"))
                }
            }
            SettingsIntent.ExportSaved -> viewModelScope.launch {
                settingsRepository.setLastBackupTimestamp(Clock.System.now().toEpochMilliseconds())
                _effect.send(SettingsEffect.ShowMessage("Backup saved successfully"))
            }
            is SettingsIntent.ImportData -> viewModelScope.launch {
                try {
                    val imported = importNotes(intent.content)
                    _effect.send(SettingsEffect.ShowMessage("Imported $imported note(s)"))
                } catch (e: Exception) {
                    _effect.send(SettingsEffect.ShowMessage("Import failed: invalid or corrupted file"))
                }
            }
        }
    }

    private fun Long.toFormattedDate(): String {
        return try {
            val instant = Instant.fromEpochMilliseconds(this)
            val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val month = local.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
            "$month ${local.dayOfMonth}, ${local.year}"
        } catch (_: Exception) {
            ""
        }
    }
}
