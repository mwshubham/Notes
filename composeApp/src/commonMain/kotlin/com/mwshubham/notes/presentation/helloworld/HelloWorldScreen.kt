package com.mwshubham.notes.presentation.helloworld

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

/**
 * Hello World screen — the "public face" of the app.
 * Completely stateless; users who don't know the secret land here permanently.
 * Styled to look like an unfinished demo app — intentionally dull.
 */
@Composable
fun HelloWorldScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello World !",
            fontSize = 28.sp,
            fontFamily = FontFamily.Default,
            color = Color.Black
        )
    }
}
