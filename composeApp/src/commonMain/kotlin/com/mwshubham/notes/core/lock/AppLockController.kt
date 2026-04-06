package com.mwshubham.notes.core.lock

import com.mwshubham.notes.core.logger.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AppLockController"
private const val LOCK_GRACE_PERIOD_MS = 30_000L

/**
 * Singleton that tracks whether the vault should be locked.
 *
 * When the app goes to background [onBackground] is called — a 30-second grace-period
 * timer starts.  If the user returns within that window [onForeground] cancels the timer.
 * Once the timer expires [isLocked] is set to `true` and the navigation layer redirects
 * to the Splash/auth screen.
 */
object AppLockController {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private var lockJob: Job? = null

    fun onBackground() {
        if (lockJob?.isActive == true) return
        AppLogger.i(TAG, "App backgrounded — starting ${LOCK_GRACE_PERIOD_MS}ms grace period")
        lockJob = scope.launch {
            delay(LOCK_GRACE_PERIOD_MS)
            AppLogger.i(TAG, "Grace period elapsed — locking vault")
            _isLocked.value = true
        }
    }

    fun onForeground() {
        if (lockJob?.isActive == true) {
            AppLogger.i(TAG, "App foregrounded within grace period — cancelling lock timer")
            lockJob?.cancel()
            lockJob = null
        }
    }

    /** Called by the navigation layer after it has handled the lock and navigated to Splash. */
    fun reset() {
        _isLocked.value = false
    }
}
