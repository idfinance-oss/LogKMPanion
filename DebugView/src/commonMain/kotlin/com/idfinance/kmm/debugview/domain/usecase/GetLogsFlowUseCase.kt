package com.idfinance.kmm.debugview.domain.usecase

import com.idfinance.kmm.debugview.domain.repository.LogRepository

internal class GetLogsFlowUseCase(
    private val repository: LogRepository,
) {

    operator fun invoke() = repository.readLogs()
}
