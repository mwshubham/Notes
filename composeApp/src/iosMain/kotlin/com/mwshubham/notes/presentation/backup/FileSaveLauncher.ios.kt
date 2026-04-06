package com.mwshubham.notes.presentation.backup

import androidx.compose.runtime.Composable

@Composable
actual fun rememberFileSaveLauncher(
    onSuccess: () -> Unit
): (suggestedFileName: String, content: String) -> Unit = { _, _ -> }
