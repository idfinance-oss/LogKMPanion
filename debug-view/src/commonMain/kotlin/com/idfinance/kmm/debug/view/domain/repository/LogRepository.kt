package com.idfinance.kmm.debug.view.domain.repository

import com.idfinance.kmm.debug.view.data.model.Log
import com.idfinance.kmm.debug.view.domain.LogType
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

internal interface LogRepository {
    /**
     *
     */
    suspend fun saveLog(type: LogType, tag: String, message: String, time: Long)

    /**
     *
     */
    fun readLogs(): Flow<ResultsChange<Log>>

    /**
     *
     */
    suspend fun clearLogs()
}
