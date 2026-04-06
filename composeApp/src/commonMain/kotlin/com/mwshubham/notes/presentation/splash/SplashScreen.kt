package com.mwshubham.notes.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mwshubham.notes.navigation.NavKey
import kotlinx.coroutines.flow.collectLatest

/**
 * Splash (disguise) screen.
 *
 * Surface appearance: plain white, boring "Hello World" — looks like a test app.
 * Hidden behaviour: each tap increments a counter; 5 taps → vault unlock.
 * After 5 s the timer fires and navigates to HelloWorld automatically.
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigate: (NavKey) -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Consume navigation effects exactly once
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SplashEffect.NavigateToHelloWorld -> onNavigate(
                    com.mwshubham.notes.navigation.HelloWorld
                )
                is SplashEffect.NavigateToNoteList -> onNavigate(
                    com.mwshubham.notes.navigation.NoteList
                )
            }
        }
    }

    // Subtle text fade-in for a polished feel even on the disguise screen
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "splash_fade"
    )

    // Invisible ripple — no visual indicator of the tap counter
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                interactionSource = interactionSource,
                indication = null,         // no ripple — secret tap
                onClick = { viewModel.onIntent(SplashIntent.OnTap) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (state.canNavigateToVault) ":)" else "Hello World!",
            modifier = Modifier.alpha(alpha),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Default,   // intentionally plain
            color = Color.Black
        )
    }
}
