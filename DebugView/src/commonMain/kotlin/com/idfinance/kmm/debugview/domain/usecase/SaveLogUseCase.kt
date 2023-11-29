package com.idfinance.kmm.debugview.domain.usecase

import com.idfinance.kmm.debugview.domain.LogType
import com.idfinance.kmm.debugview.domain.repository.LogRepository

internal class SaveLogUseCase(
    private val repository: LogRepository,
) {
    suspend operator fun invoke(payload: Payload) {
        repository.saveLog(payload.type, payload.tag, payload.message, payload.time)
    }

    class Payload(
        val type: LogType,
        val tag: String,
        val message: String,
        val time: Long,
    )
}
