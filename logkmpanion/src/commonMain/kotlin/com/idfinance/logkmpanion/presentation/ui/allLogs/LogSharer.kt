package com.idfinance.logkmpanion.presentation.ui.allLogs

internal interface LogSharer {
    suspend fun shareAsFile(content: String, fileName: String)
}
