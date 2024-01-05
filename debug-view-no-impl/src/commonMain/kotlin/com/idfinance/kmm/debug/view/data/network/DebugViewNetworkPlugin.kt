package com.idfinance.kmm.debug.view.data.network

import io.ktor.client.plugins.api.createClientPlugin

fun debugViewNetworkPlugin(sessionId: String = "") = createClientPlugin("DebugViewNetworkPlugin") {}