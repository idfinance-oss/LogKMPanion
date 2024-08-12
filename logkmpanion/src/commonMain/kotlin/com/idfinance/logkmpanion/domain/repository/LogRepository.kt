package com.idfinance.logkmpanion.domain.repository

import com.idfinance.logkmpanion.data.model.Log
import com.idfinance.logkmpanion.data.model.NetworkRequest
import com.idfinance.logkmpanion.data.model.Request
import com.idfinance.logkmpanion.data.model.Response
import com.idfinance.logkmpanion.data.model.Session
import com.idfinance.logkmpanion.domain.LogType
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

internal interface LogRepository {
    /**
     *
     */
    suspend fun saveLog(type: LogType, tag: String, message: String, time: Long)

    suspend fun saveRequest(uuid: String, sessionId: String, params: Request)

    suspend fun saveResponse(uuid: String, duration: Long, params: Response)

    fun readNetworkLogById(id: String): Flow<SingleQueryChange<NetworkRequest>>

    /**
     *
     */
    fun readAllLogs(): Flow<ResultsChange<Log>>

    /**
     *
     */
    fun readNetworkLogs(): Flow<ResultsChange<NetworkRequest>>

    fun readSessions(): Flow<ResultsChange<Session>>

    /**
     *
     */
    suspend fun clearAllLogs()

    suspend fun clearNetworkLogs()

    suspend fun saveErrorMessage(id: String, message: String?)
}
