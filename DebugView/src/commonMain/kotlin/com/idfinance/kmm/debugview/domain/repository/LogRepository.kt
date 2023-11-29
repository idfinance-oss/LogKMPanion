package com.idfinance.kmm.debugview.domain.repository

import com.idfinance.kmm.debugview.data.model.Log
import com.idfinance.kmm.debugview.domain.LogType
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
