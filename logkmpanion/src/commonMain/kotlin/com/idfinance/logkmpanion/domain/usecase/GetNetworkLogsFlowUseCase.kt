package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.presentation.extensions.toUIFormat
import com.idfinance.logkmpanion.data.model.GroupedNetworkLogs
import com.idfinance.logkmpanion.data.model.NetworkRequest
import com.idfinance.logkmpanion.data.model.Session
import com.idfinance.logkmpanion.domain.repository.LogRepository
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class GetNetworkLogsFlowUseCase(
    private val repository: LogRepository
) {

    operator fun invoke(): Flow<List<GroupedNetworkLogs>> {
        val networkLogsFlow = repository.readNetworkLogs()
        val sessionsFlow = repository.readSessions()

        return networkLogsFlow.combine(sessionsFlow) { logsItem, sessionsItem ->
            val logs: List<NetworkRequest> = logsItem.list
            val sessions: List<Session> = sessionsItem.list
            sessions.map { session ->
                GroupedNetworkLogs(
                    GMTDate(session.timeStart).toUIFormat(),
                    logs.filter { it.sessionId == session._id }
                )
            }
        }
    }
}
