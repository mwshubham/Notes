package com.mwshubham.notes.di

import org.koin.core.context.startKoin

actual fun initKoin() {
    // Android uses KoinApplication started in MainActivity with androidContext().
    // This actual is intentionally empty — MainActivity calls startKoin directly
    // so it can supply the Android Context.
}
