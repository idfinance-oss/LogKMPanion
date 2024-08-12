package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class ClearNetworkLogsUseCase(
    private val repository: LogRepository,
) {

    suspend operator fun invoke() = repository.clearNetworkLogs()
}