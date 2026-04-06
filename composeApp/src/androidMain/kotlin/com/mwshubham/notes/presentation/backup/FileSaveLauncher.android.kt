package com.mwshubham.notes.presentation.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFileSaveLauncher(
    onSuccess: () -> Unit
): (suggestedFileName: String, content: String) -> Unit {
    val context = LocalContext.current
    val pendingContent = remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            val content = pendingContent.value
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(content.toByteArray(Charsets.UTF_8))
            }
            onSuccess()
        }
    }

    return { suggestedFileName, content ->
        pendingContent.value = content
        launcher.launch(suggestedFileName)
    }
}
