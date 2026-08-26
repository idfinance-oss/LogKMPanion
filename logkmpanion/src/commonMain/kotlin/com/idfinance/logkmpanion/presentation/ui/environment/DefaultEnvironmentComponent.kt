package com.idfinance.logkmpanion.presentation.ui.environment

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider
import com.idfinance.logkmpanion.presentation.extensions.disposableScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class DefaultEnvironmentComponent(
    context: ComponentContext,
    private val provider: EnvironmentProvider,
    coroutineContext: CoroutineContext = Dispatchers.Main,
) : EnvironmentComponent,
    ComponentContext by context,
    CoroutineScope by context.disposableScope(coroutineContext) {

    private val _model = MutableValue(
        EnvironmentComponent.Model(
            environments = provider.environments,
            customHost = provider.environments.firstOrNull { it.isHostEditable }?.host.orEmpty(),
        )
    )
    override val model: Value<EnvironmentComponent.Model> = _model

    init {
        var currentJob: Job? = null
        lifecycle.doOnStart {
            currentJob = launch {
                provider.current.collectLatest { current ->
                    _model.update {
                        val isFirstEmission = it.selectedId == null
                        it.copy(
                            current = current,
                            selectedId = it.selectedId ?: current.id,
                            /**
                             * The applied host is the source of truth for an editable entry: the
                             * [EnvironmentProvider.environments] template may still carry the value
                             * the app was built with. Seeded once so typing is never overwritten.
                             */
                            customHost = if (isFirstEmission && current.isHostEditable) {
                                current.host
                            } else {
                                it.customHost
                            },
                        )
                    }
                }
            }
        }
        lifecycle.doOnStop {
            currentJob?.cancel()
        }
    }

    override fun selectEnvironment(id: String) {
        _model.update { it.copy(selectedId = id, error = null) }
    }

    override fun changeCustomHost(host: String) {
        _model.update { it.copy(customHost = host, error = null) }
    }

    override fun apply() {
        val model = _model.value
        val environment = model.selected ?: return
        if (!model.isApplyEnabled) return

        _model.update { it.copy(isApplying = true, error = null) }
        launch {
            try {
                provider.select(
                    id = environment.id,
                    host = model.customHost.trim().takeIf { environment.isHostEditable },
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                _model.update { it.copy(error = e.message ?: e::class.simpleName) }
            } finally {
                /**
                 * Never runs when the host app kills the process inside [EnvironmentProvider.select]
                 * to rebuild its network stack, which is the expected outcome for most apps.
                 */
                _model.update { it.copy(isApplying = false) }
            }
        }
    }
}
