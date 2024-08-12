package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class GetAllLogsFlowUseCase(
    private val repository: LogRepository
) {

    operator fun invoke() = repository.readAllLogs()
}
