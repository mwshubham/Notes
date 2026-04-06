package com.mwshubham.notes

import androidx.compose.ui.window.ComposeUIViewController
import com.mwshubham.notes.di.initKoin

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS before the first composable renders
    initKoin()
    App()
}