package com.idfinance.kmm.debug.view.data.repository

import com.idfinance.kmm.debug.view.data.model.Log
import com.idfinance.kmm.debug.view.domain.LogType
import com.idfinance.kmm.debug.view.domain.repository.LogRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

internal class LogRepository(
    private val realm: Realm
) : LogRepository {

    override suspend fun saveLog(type: LogType, tag: String, message: String, time: Long) {
        realm.write { copyToRealm(Log(tag, message, type, time)) }
    }

    override fun readLogs() = realm.query<Log>().find().asFlow()
    override suspend fun clearLogs() {
        realm.write { deleteAll() }
    }
}