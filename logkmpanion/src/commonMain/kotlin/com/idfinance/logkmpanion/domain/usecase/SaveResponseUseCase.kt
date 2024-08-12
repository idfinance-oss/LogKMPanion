package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.data.model.Response
import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class SaveResponseUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(uuid: String, duration: Long, response: Response) {
        repository.saveResponse(uuid, duration, response)
    }
}