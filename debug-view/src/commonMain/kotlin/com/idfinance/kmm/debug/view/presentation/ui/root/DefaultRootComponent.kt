package com.idfinance.kmm.debug.view.presentation.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.presentation.extensions.disposableScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

internal class DefaultRootComponent(
    context: ComponentContext,
    private val onClose: () -> Unit,
) : RootComponent,
    ComponentContext by context,
    CoroutineScope by context.disposableScope() {

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.AllLogs) },
        childFactory = { config, context ->
            when (config) {
                Config.AllLogs -> {
                    val component = ServiceLocator.getAllLogsComponent(context)
                    RootComponent.Child.AllLogs(component)
                }

                Config.NetworkLogs -> {
                    val component = ServiceLocator.getNetworkLogsComponent(context)
                    RootComponent.Child.NetworkLogs(component)
                }
            }
        }
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = stack

    override fun close() {
        onClose()
    }

    override fun openAllLogs() {
        navigation.replaceAll(Config.AllLogs)
    }

    override fun openNetworkLogs() {
        navigation.replaceAll(Config.NetworkLogs)
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object AllLogs : Config

        @Serializable
        data object NetworkLogs : Config
    }
}
