package com.idfinance.logkmpanion.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.idfinance.logkmpanion.presentation.ui.theme.colorScheme

@Composable
internal fun DebugViewTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(content = content, colorScheme = colorScheme())
}
