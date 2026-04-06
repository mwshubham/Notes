package com.mwshubham.notes.core.lock

import com.mwshubham.notes.core.logger.AppLogger
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillEnterForegroundNotification

private const val TAG = "AppLifecycleObserver"

/**
 * Registers [NSNotificationCenter] observers for iOS app lifecycle events.
 * Call once at app startup (from [com.mwshubham.notes.di.KoinInitializer]).
 */
fun registerAppLifecycleObservers() {
    val center = NSNotificationCenter.defaultCenter

    center.addObserverForName(
        name = UIApplicationDidEnterBackgroundNotification,
        `object` = null,
        queue = NSOperationQueue.mainQueue
    ) {
        AppLogger.i(TAG, "iOS app entered background")
        AppLockController.onBackground()
    }

    center.addObserverForName(
        name = UIApplicationWillEnterForegroundNotification,
        `object` = null,
        queue = NSOperationQueue.mainQueue
    ) {
        AppLogger.i(TAG, "iOS app will enter foreground")
        AppLockController.onForeground()
    }
}
