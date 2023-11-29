package com.idfinance.kmm.debugview.data.repository

import com.idfinance.kmm.debugview.data.model.Log
import com.idfinance.kmm.debugview.domain.LogType
import com.idfinance.kmm.debugview.domain.repository.LogRepository
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