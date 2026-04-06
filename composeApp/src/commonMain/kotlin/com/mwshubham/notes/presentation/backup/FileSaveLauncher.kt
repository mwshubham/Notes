package com.mwshubham.notes.presentation.backup

import androidx.compose.runtime.Composable

/**
 * Returns a lambda that, when invoked with (suggestedFileName, content), opens the platform
 * file-save dialog and writes [content] to the user-chosen location.
 * [onSuccess] is called after the file is written successfully.
 */
@Composable
expect fun rememberFileSaveLauncher(
    onSuccess: () -> Unit
): (suggestedFileName: String, content: String) -> Unit
