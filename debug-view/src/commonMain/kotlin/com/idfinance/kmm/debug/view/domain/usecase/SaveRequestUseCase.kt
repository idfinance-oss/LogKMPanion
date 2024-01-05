package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.data.model.Request
import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class SaveRequestUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(uuid: String, sessionId: String, params: Request) {
        repository.saveRequest(uuid, sessionId, params)
    }
}