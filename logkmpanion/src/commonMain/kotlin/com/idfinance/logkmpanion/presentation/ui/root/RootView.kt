package com.idfinance.logkmpanion.presentation.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.idfinance.logkmpanion.presentation.ui.theme.DebugViewTheme
import com.idfinance.logkmpanion.presentation.ui.allLogs.AllLogsView
import com.idfinance.logkmpanion.presentation.ui.networkLogs.NetworkLogsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RootView(component: RootComponent) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    DebugViewTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("DebugView") },
                        navigationIcon = {
                            IconButton(onClick = component::close) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = activeComponent is RootComponent.Child.AllLogs,
                            onClick = component::openAllLogs,
                            icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                            label = { Text("All logs") }
                        )
                        NavigationBarItem(
                            selected = activeComponent is RootComponent.Child.NetworkLogs,
                            onClick = component::openNetworkLogs,
                            icon = { Icon(Icons.Default.NetworkCheck, null) },
                            label = { Text("Network logs") }
                        )
                    }
                }
            ) {
                Children(component.childStack, modifier = Modifier.padding(it)) {
                    when (val child = it.instance) {
                        is RootComponent.Child.AllLogs -> AllLogsView(child.component)
                        is RootComponent.Child.NetworkLogs -> NetworkLogsView(child.component)
                    }
                }
            }
        }
    }
}
