package com.mwshubham.notes.di

import com.mwshubham.notes.core.lock.registerAppLifecycleObservers
import org.koin.core.context.startKoin

actual fun initKoin() {
    registerAppLifecycleObservers()
    startKoin {
        modules(appModules)
    }
}
