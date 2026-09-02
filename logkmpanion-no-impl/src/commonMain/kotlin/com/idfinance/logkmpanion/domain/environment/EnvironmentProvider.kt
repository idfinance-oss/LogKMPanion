package com.idfinance.logkmpanion.domain.environment

import kotlinx.coroutines.flow.Flow

data class DebugEnvironment(
    val id: String,
    val title: String,
    val host: String,
    val isHostEditable: Boolean = false,
)

interface EnvironmentProvider {

    val environments: List<DebugEnvironment>

    val current: Flow<DebugEnvironment>

    suspend fun select(id: String, host: String? = null)
}
