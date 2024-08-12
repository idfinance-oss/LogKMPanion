package com.idfinance.logkmpanion.data.model

internal class GroupedNetworkLogs(
    val sessionStartTime: String,
    val logs: List<NetworkRequest>
)