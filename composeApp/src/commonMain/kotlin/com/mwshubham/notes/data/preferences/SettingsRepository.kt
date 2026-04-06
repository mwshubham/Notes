package com.mwshubham.notes.data.preferences

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val tapThreshold: Flow<Int>
    suspend fun setTapThreshold(value: Int)
}
