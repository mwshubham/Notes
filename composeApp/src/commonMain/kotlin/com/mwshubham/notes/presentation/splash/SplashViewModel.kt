package com.mwshubham.notes.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwshubham.notes.core.logger.AppLogger
import com.mwshubham.notes.data.preferences.DEFAULT_TAP_THRESHOLD
import com.mwshubham.notes.data.preferences.SettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SplashViewModel"
private const val AUTO_NAVIGATE_DELAY_MS = 5_000L

class SplashViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    // Channel so each effect is consumed exactly once (no buffering beyond capacity 1)
    private val _effect = Channel<SplashEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    // Live tap threshold backed by DataStore; starts at the compile-time default
    private val _tapThreshold = MutableStateFlow(DEFAULT_TAP_THRESHOLD)

    init {
        observeTapThreshold()
        startAutoNavigateTimer()
    }

    private fun observeTapThreshold() {
        viewModelScope.launch {
            settingsRepository.tapThreshold.collect { threshold ->
                _tapThreshold.value = threshold
                AppLogger.d(TAG, "Tap threshold updated — $threshold")
            }
        }
    }

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.OnTap -> handleTap()
            is SplashIntent.OnTimerElapsed -> navigateToHelloWorld()
        }
    }

    private fun startAutoNavigateTimer() {
        viewModelScope.launch {
            AppLogger.i(TAG, "Auto-navigate timer started (${AUTO_NAVIGATE_DELAY_MS}ms)")
            delay(AUTO_NAVIGATE_DELAY_MS)
            onIntent(SplashIntent.OnTimerElapsed)
        }
    }

    private fun handleTap() {
        if (!_state.value.canNavigateToVault) {
            AppLogger.d(TAG, "Tap ignored - vault navigation disabled")
            return
        }

        val threshold = _tapThreshold.value
        val current = _state.value.tapCount + 1
        AppLogger.d(TAG, "Tap detected — count=$current / $threshold")
        _state.update { it.copy(tapCount = current) }
        if (current >= threshold) {
            AppLogger.i(TAG, "Secret threshold reached — unlocking vault")
            navigateToNoteList()
        }
    }

    private fun navigateToHelloWorld() {
        _state.update { it.copy(canNavigateToVault = false) }
        viewModelScope.launch {
            AppLogger.i(TAG, "Navigating to HelloWorld (time-out)")
            _effect.send(SplashEffect.NavigateToHelloWorld)
        }
    }

    private fun navigateToNoteList() {
        viewModelScope.launch {
            AppLogger.i(TAG, "Navigating to NoteList (secret unlock)")
            _effect.send(SplashEffect.NavigateToNoteList)
        }
    }
}
