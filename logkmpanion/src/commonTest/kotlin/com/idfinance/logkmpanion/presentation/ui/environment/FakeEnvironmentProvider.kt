package com.idfinance.logkmpanion.presentation.ui.environment

import com.idfinance.logkmpanion.domain.environment.DebugEnvironment
import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal val TEST_ENVIRONMENTS = listOf(
    DebugEnvironment(id = "master", title = "Master", host = "master.example.com"),
    DebugEnvironment(id = "prod", title = "Production", host = "example.com"),
    DebugEnvironment(id = "custom", title = "Custom", host = "pr-1.example.com", isHostEditable = true),
)

internal class FakeEnvironmentProvider(
    override val environments: List<DebugEnvironment> = TEST_ENVIRONMENTS,
    private val failure: Throwable? = null,
    /** When set, [select] suspends on it, so a test can observe the in-flight state. */
    private val gate: CompletableDeferred<Unit>? = null,
    initialCurrent: DebugEnvironment? = null,
) : EnvironmentProvider {

    val selectCalls = mutableListOf<SelectCall>()

    private val _current = MutableStateFlow(initialCurrent ?: environments.first())
    override val current: Flow<DebugEnvironment> = _current

    override suspend fun select(id: String, host: String?) {
        selectCalls += SelectCall(id, host)
        gate?.await()
        failure?.let { throw it }
        val environment = environments.first { it.id == id }
        _current.value = if (host != null) environment.copy(host = host) else environment
    }

    fun emitCurrent(id: String) {
        _current.value = environments.first { it.id == id }
    }

    data class SelectCall(val id: String, val host: String?)
}
