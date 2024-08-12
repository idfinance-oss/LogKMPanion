package com.idfinance.logkmpanion.data.network

import io.ktor.client.plugins.api.createClientPlugin

fun logKMPanionNetworkPlugin(sessionId: String = "") = createClientPlugin("DebugViewNetworkPlugin") {}