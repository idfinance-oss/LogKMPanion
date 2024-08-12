package com.idfinance.logkmpanion.presentation // ktlint-disable filename

import androidx.compose.ui.window.ComposeUIViewController
import com.idfinance.logkmpanion.presentation.ui.root.RootComponent
import com.idfinance.logkmpanion.presentation.ui.root.RootView

@Suppress("FunctionName")
internal fun ComposeDebugViewViewController(component: RootComponent) = ComposeUIViewController {
    RootView(component)
}
