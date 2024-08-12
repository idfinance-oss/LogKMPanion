package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.data.model.Request
import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class SaveRequestUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(uuid: String, sessionId: String, params: Request) {
        repository.saveRequest(uuid, sessionId, params)
    }
}