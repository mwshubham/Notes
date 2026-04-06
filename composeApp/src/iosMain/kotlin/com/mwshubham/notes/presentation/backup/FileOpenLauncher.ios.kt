package com.mwshubham.notes.presentation.backup

import androidx.compose.runtime.Composable

@Composable
actual fun rememberFileOpenLauncher(
    onFileOpened: (String?) -> Unit
): () -> Unit = {}
