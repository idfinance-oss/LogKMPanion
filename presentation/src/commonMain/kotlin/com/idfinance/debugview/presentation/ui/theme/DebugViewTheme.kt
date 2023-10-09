package com.idfinance.debugview.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DebugViewTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(content = content)
}