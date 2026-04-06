package com.mwshubham.notes.core.lock

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Registers as a [DefaultLifecycleObserver] on the [ProcessLifecycleOwner] so the
 * whole-process lifecycle (rather than a single Activity) is observed.
 *
 * Register in [com.mwshubham.notes.MainActivity.onCreate] via:
 *   ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())
 */
class AppLifecycleObserver : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        AppLockController.onBackground()
    }

    override fun onStart(owner: LifecycleOwner) {
        AppLockController.onForeground()
    }
}
