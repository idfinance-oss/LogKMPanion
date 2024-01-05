package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class GetNetworkLogByIdFlowUseCase(
    private val repository: LogRepository
) {

    operator fun invoke(id: String) = repository.readNetworkLogById(id)
}