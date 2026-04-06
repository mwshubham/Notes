package com.mwshubham.notes

import androidx.compose.runtime.Composable

/**
 * Platform-specific composition root.
 * On Android: uses Nav3 [NavDisplay] with the full backstack.
 * On iOS: can use Nav3 runtime or a simpler state-driven approach.
 */
@Composable
expect fun App()