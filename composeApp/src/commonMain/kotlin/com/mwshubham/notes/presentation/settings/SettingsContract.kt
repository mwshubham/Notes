package com.mwshubham.notes.presentation.settings

import com.mwshubham.notes.data.preferences.DEFAULT_TAP_THRESHOLD

data class SettingsState(
    val tapThreshold: Int = DEFAULT_TAP_THRESHOLD
)

sealed interface SettingsIntent {
    data class SetTapThreshold(val value: Int) : SettingsIntent
}

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
}
