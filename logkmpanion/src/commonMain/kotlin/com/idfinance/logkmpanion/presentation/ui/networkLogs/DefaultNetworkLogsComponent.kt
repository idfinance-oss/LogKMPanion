package com.idfinance.logkmpanion.presentation.ui.networkLogs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.idfinance.logkmpanion.presentation.ui.ModalComponent
import com.idfinance.logkmpanion.ServiceLocator
import com.idfinance.logkmpanion.domain.usecase.ClearNetworkLogsUseCase
import com.idfinance.logkmpanion.domain.usecase.GetNetworkLogsFlowUseCase
import com.idfinance.logkmpanion.presentation.extensions.disposableScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

internal class DefaultNetworkLogsComponent(
    context: ComponentContext,
    getNetworkLogsFlowUseCase: GetNetworkLogsFlowUseCase,
    private val clearLogsUseCase: ClearNetworkLogsUseCase
) : NetworkLogsComponent,
    ComponentContext by context,
    CoroutineScope by context.disposableScope() {

    private val _model = MutableValue(NetworkLogsComponent.Model())
    override val model: Value<NetworkLogsComponent.Model> = _model

    private val modalNavigation = SlotNavigation<NetworkLogDetailsConfig>()

    override val modal: Value<ChildSlot<*, ModalComponent>> = childSlot(
        source = modalNavigation,
        serializer = NetworkLogDetailsConfig.serializer(),
        handleBackButton = true
    ) { config, context ->
        ServiceLocator.getNetworkLogDetailsComponent(config.id, context, modalNavigation::dismiss)
    }

    init {
        val logsFlow = getNetworkLogsFlowUseCase()
        var logsFlowJob: Job? = null
        lifecycle.doOnStart {
            logsFlowJob = launch {
                logsFlow.collectLatest {
                    _model.value = _model.value.copy(logs = it)
                }
            }
        }
        lifecycle.doOnStop {
            logsFlowJob?.cancel()
        }
    }

    override fun clearLogs() {
        launch { clearLogsUseCase() }
    }

    override fun openRequestDetails(id: String) {
        modalNavigation.activate(NetworkLogDetailsConfig(id))
    }

    @Serializable
    private data class NetworkLogDetailsConfig(val id: String)
}