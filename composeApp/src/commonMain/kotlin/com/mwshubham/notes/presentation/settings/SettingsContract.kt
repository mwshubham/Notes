package com.mwshubham.notes.presentation.settings

import com.mwshubham.notes.data.preferences.DEFAULT_TAP_THRESHOLD

data class SettingsState(
    val tapThreshold: Int = DEFAULT_TAP_THRESHOLD,
    val lastBackupLabel: String? = null
)

sealed interface SettingsIntent {
    data class SetTapThreshold(val value: Int) : SettingsIntent
    data object ExportClicked : SettingsIntent
    data object ExportSaved : SettingsIntent
    data class ImportData(val content: String) : SettingsIntent
}

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
    data class LaunchExport(val suggestedFileName: String, val content: String) : SettingsEffect
    data class ShowMessage(val text: String) : SettingsEffect
}
