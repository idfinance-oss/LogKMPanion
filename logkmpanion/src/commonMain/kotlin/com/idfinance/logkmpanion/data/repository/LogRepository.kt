package com.idfinance.logkmpanion.data.repository

import com.idfinance.logkmpanion.data.model.Log
import com.idfinance.logkmpanion.data.model.NetworkRequest
import com.idfinance.logkmpanion.data.model.Request
import com.idfinance.logkmpanion.data.model.Response
import com.idfinance.logkmpanion.data.model.Session
import com.idfinance.logkmpanion.domain.LogType
import com.idfinance.logkmpanion.domain.repository.LogRepository
import io.ktor.util.date.GMTDate
import io.realm.kotlin.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query

internal class LogRepository(
    private val realm: Realm
) : LogRepository {

    override suspend fun saveLog(type: LogType, tag: String, message: String, time: Long) {
        realm.write { copyToRealm(Log(tag, message, type, time)) }
    }

    override suspend fun saveRequest(uuid: String, sessionId: String, params: Request) {
        val record = NetworkRequest().apply {
            _id = uuid
            this.sessionId = sessionId
            request = params
        }
        realm.write {
            copyToRealm(record)
            val session = query<Session>("_id == $0", sessionId).first().find()
            if (session == null) {
                val sessionRecord = Session().apply {
                    this._id = sessionId
                    this.timeStart = GMTDate().timestamp
                }
                copyToRealm(sessionRecord)
            }
        }
    }

    override suspend fun saveResponse(uuid: String, duration: Long, params: Response) {
        realm.write {
            val record = query<NetworkRequest>("_id == $0", uuid).first().find()
            record?.response = params
            record?.duration = duration
        }
    }

    override fun readNetworkLogById(id: String) =
        realm.query<NetworkRequest>("_id = $0", id).first().asFlow()

    override fun readAllLogs() = realm.query<Log>().find().asFlow()

    override fun readNetworkLogs() = realm.query<NetworkRequest>().find().asFlow()

    override fun readSessions() = realm.query<Session>().find().asFlow()

    override suspend fun clearAllLogs() {
        realm.write { delete<Log>() }
    }

    override suspend fun clearNetworkLogs() {
        realm.write { delete<NetworkRequest>() }
    }

    override suspend fun saveErrorMessage(id: String, message: String?) {
        realm.write {
            val record = query<NetworkRequest>("_id == $0", id).first().find()
            record?.error = message
        }
    }
}