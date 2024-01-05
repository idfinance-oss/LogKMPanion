package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.data.model.Response
import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class SaveResponseUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(uuid: String, duration: Long, response: Response) {
        repository.saveResponse(uuid, duration, response)
    }
}