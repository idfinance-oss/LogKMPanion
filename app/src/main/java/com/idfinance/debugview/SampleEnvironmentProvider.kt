package com.idfinance.debugview

import com.idfinance.logkmpanion.domain.environment.DebugEnvironment
import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Minimal in-memory provider demonstrating the Environment tab.
 *
 * A real app persists the choice and rebuilds its network stack — usually by restarting itself —
 * inside [select]; this one only keeps the value in memory so the switch is visible immediately.
 */
object SampleEnvironmentProvider : EnvironmentProvider {

    override val environments = listOf(
        DebugEnvironment(id = "staging", title = "Staging", host = "staging.example.com"),
        DebugEnvironment(id = "production", title = "Production", host = "example.com"),
        DebugEnvironment(id = "custom", title = "Custom host", host = "", isHostEditable = true),
    )

    private val _current = MutableStateFlow(environments.first())
    override val current: Flow<DebugEnvironment> = _current

    override suspend fun select(id: String, host: String?) {
        val environment = environments.first { it.id == id }
        _current.value = if (host != null) environment.copy(host = host) else environment
    }
}
