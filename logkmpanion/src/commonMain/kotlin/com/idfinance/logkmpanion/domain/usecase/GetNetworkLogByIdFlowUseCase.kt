package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class GetNetworkLogByIdFlowUseCase(
    private val repository: LogRepository
) {

    operator fun invoke(id: String) = repository.readNetworkLogById(id)
}