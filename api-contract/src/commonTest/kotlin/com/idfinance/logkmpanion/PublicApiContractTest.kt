package com.idfinance.logkmpanion

import com.idfinance.logkmpanion.data.network.logKMPanionNetworkPlugin
import com.idfinance.logkmpanion.domain.LogType
import com.idfinance.logkmpanion.domain.addToLogKMPanion
import com.idfinance.logkmpanion.domain.environment.DebugEnvironment
import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.test.Test
import kotlin.test.assertEquals

class PublicApiContractTest {

    @Test
    fun logTypeEntries_keepTheOrderOfTheFullLibrary() {
        assertEquals(listOf("DEFAULT", "ERROR"), LogType.entries.map { it.name })
    }

    @Suppress("unused")
    private suspend fun callEveryCommonApi(provider: EnvironmentProvider): Flow<DebugEnvironment> {
        addToLogKMPanion(type = LogType.ERROR, tag = "tag", message = "message")
        logKMPanionNetworkPlugin(sessionId = "session")
        LogKMPanion.setEnvironmentProvider(provider = ContractEnvironmentProvider())
        provider.select(id = provider.environments.first().id, host = null)
        DebugEnvironment(id = "id", title = "title", host = "host", isHostEditable = true)
        return provider.current
    }

    private class ContractEnvironmentProvider : EnvironmentProvider {
        override val environments: List<DebugEnvironment> = emptyList()
        override val current: Flow<DebugEnvironment> = emptyFlow()
        override suspend fun select(id: String, host: String?) = Unit
    }
}
