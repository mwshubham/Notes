package com.mwshubham.notes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Vault colour palette ─────────────────────────────────────────────────────
private val InkNavy       = Color(0xFF0B0E18)
private val DarkSlate     = Color(0xFF141826)
private val ElevatedDark  = Color(0xFF1E2336)
private val ElectricViolet = Color(0xFF7C6BFF)
private val VioletPressed  = Color(0xFF5A4BCC)
private val CipherTeal    = Color(0xFF3DE6C8)
private val SoftWhite     = Color(0xFFE8EAF6)
private val MutedLavender = Color(0xFFB0B8D8)
private val CoralRed      = Color(0xFFFF6B8A)
private val OnViolet      = Color(0xFFFFFFFF)

private val VaultColorScheme = darkColorScheme(
    primary            = ElectricViolet,
    onPrimary          = OnViolet,
    primaryContainer   = VioletPressed,
    onPrimaryContainer = SoftWhite,
    secondary          = CipherTeal,
    onSecondary        = InkNavy,
    background         = InkNavy,
    onBackground       = SoftWhite,
    surface            = DarkSlate,
    onSurface          = SoftWhite,
    surfaceVariant     = ElevatedDark,
    onSurfaceVariant   = MutedLavender,
    error              = CoralRed,
    onError            = OnViolet,
    outline            = MutedLavender.copy(alpha = 0.4f)
)

/**
 * Dark Material3 theme applied exclusively to vault screens (NoteList, NoteDetail).
 * Splash and HelloWorld use a plain bright MaterialTheme — intentional disguise.
 */
@Composable
fun VaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VaultColorScheme,
        typography = VaultTypography,
        content = content
    )
}

/**
 * Bright/plain theme used for the disguise screens (Splash, HelloWorld).
 * Intentionally non-remarkable so the app looks like a throwaway demo.
 */
@Composable
fun DisguiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
