package com.idfinance.logkmpanion.presentation.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.idfinance.logkmpanion.presentation.ui.allLogs.AllLogsComponent
import com.idfinance.logkmpanion.presentation.ui.networkLogs.NetworkLogsComponent

internal interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun close()
    fun openAllLogs()
    fun openNetworkLogs()

    sealed class Child {
        class AllLogs(val component: AllLogsComponent) : Child()
        class NetworkLogs(val component: NetworkLogsComponent) : Child()
    }
}
