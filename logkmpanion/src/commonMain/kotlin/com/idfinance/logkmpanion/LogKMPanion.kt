package com.idfinance.logkmpanion

import com.idfinance.logkmpanion.domain.environment.EnvironmentProvider

/**
 * Entry point for the optional LogKMPanion features a host app opts into.
 */
object LogKMPanion {

    /**
     * Registers the [provider] backing the panel's Environment tab, or `null` to remove it.
     *
     * Call it once during app startup, before the panel can be opened; without a provider the tab
     * is hidden and the panel behaves exactly as it did before the feature existed.
     */
    fun setEnvironmentProvider(provider: EnvironmentProvider?) {
        ServiceLocator.environmentProvider = provider
    }
}
