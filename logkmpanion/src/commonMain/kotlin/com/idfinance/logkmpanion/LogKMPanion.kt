package com.idfinance.logkmpanion

import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider

object LogKMPanion {

    fun setEnvironmentProvider(provider: EnvironmentProvider?) {
        ServiceLocator.environmentProvider = provider
    }
}
