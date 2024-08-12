package com.idfinance.logkmpanion.presentation.ui.allLogs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.idfinance.logkmpanion.domain.usecase.ClearLogsUseCase
import com.idfinance.logkmpanion.domain.usecase.GetAllLogsFlowUseCase
import com.idfinance.logkmpanion.presentation.extensions.disposableScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class DefaultAllLogsComponent(
    context: ComponentContext,
    getAllLogsFlowUseCase: GetAllLogsFlowUseCase,
    private val clearLogsUseCase: ClearLogsUseCase
) : AllLogsComponent,
    ComponentContext by context,
    CoroutineScope by context.disposableScope() {

    private val _model = MutableValue(AllLogsComponent.Model())
    override val model: Value<AllLogsComponent.Model> = _model

    init {
        val logsFlow = getAllLogsFlowUseCase()
        var logsFlowJob: Job? = null
        lifecycle.doOnStart {
            logsFlowJob = launch {
                logsFlow.collectLatest {
                    _model.value = _model.value.copy(logs = it.list)
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
}