package com.mwshubham.notes.presentation.splash

/** The single source-of-truth state rendered by SplashScreen. */
data class SplashState(
    val tapCount: Int = 0,
    val canNavigateToVault: Boolean = true
)

/** All user actions the splash screen can raise. */
sealed interface SplashIntent {
    data object OnTap : SplashIntent
    data object OnTimerElapsed : SplashIntent
}

/** One-shot side effects emitted by SplashViewModel — consumed exactly once. */
sealed interface SplashEffect {
    data object NavigateToHelloWorld : SplashEffect
    data object NavigateToNoteList : SplashEffect
}
