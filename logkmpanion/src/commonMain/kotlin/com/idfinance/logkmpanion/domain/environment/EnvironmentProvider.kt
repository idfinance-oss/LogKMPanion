package com.idfinance.logkmpanion.domain.environment

import kotlinx.coroutines.flow.Flow

/**
 * A backend environment the host app can point itself at.
 *
 * @param id stable identifier, passed back to [EnvironmentProvider.select].
 * @param title human readable name shown in the panel.
 * @param host host of this environment, e.g. `my-app-master.example.com`.
 * @param isHostEditable when `true` the panel renders an editable host field for this entry
 * and passes the entered value to [EnvironmentProvider.select]. Meant for ad-hoc environments
 * such as per-PR review hosts.
 */
data class DebugEnvironment(
    val id: String,
    val title: String,
    val host: String,
    val isHostEditable: Boolean = false,
)

/**
 * Supplies the environment list and applies the switch.
 *
 * Implemented by the host app: the library knows nothing about hosts, storage or how a switch
 * is applied. Register it once at startup via [com.idfinance.logkmpanion.LogKMPanion
 * .setEnvironmentProvider]; while no provider is registered the panel hides its Environment tab.
 */
interface EnvironmentProvider {

    /** Environments offered to the user, in display order. */
    val environments: List<DebugEnvironment>

    /** Currently selected environment. The panel reflects every emission. */
    val current: Flow<DebugEnvironment>

    /**
     * Applies the environment identified by [id].
     *
     * [host] is non-null only for entries with [DebugEnvironment.isHostEditable] set, and carries
     * the value the user typed. Applying a switch may terminate the process — an app that rebuilds
     * its network stack by restarting itself will not return from this call.
     */
    suspend fun select(id: String, host: String? = null)
}
