package com.mwshubham.notes.presentation.backup

import androidx.compose.runtime.Composable

/**
 * Returns a lambda that, when invoked, opens the platform file-open dialog.
 * [onFileOpened] is called with the file content string, or null if cancelled/error.
 */
@Composable
expect fun rememberFileOpenLauncher(
    onFileOpened: (String?) -> Unit
): () -> Unit
