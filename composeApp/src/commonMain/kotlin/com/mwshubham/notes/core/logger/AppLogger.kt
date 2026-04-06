package com.mwshubham.notes.core.logger

import co.touchlab.kermit.Logger

/**
 * AppLogger — thin wrapper around Kermit for consistent logging across all layers.
 * Use [tag] to identify the caller (typically the class name).
 */
object AppLogger {

    fun d(tag: String, msg: String) {
        Logger.d(tag) { msg }
    }

    fun i(tag: String, msg: String) {
        Logger.i(tag) { msg }
    }

    fun w(tag: String, msg: String) {
        Logger.w(tag) { msg }
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        Logger.e(tag, throwable) { msg }
    }
}
