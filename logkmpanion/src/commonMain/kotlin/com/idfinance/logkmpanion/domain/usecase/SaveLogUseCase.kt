package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.domain.LogType
import com.idfinance.logkmpanion.domain.repository.LogRepository

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
