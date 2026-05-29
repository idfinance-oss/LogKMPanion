package com.idfinance.logkmpanion.presentation.ui.allLogs

import com.arkivanov.decompose.value.Value
import com.idfinance.logkmpanion.data.model.Log

internal interface AllLogsComponent {
    val model: Value<Model>

    fun clearLogs()
    fun shareFullLog()

    data class Model(val logs: List<Log> = emptyList()) {
        internal val full: String
            get() = logs.joinToString("\n") { "[${it.tag}] ${it.message}" }
        val clipboardPayload: ClipboardPayload
            get() = tailWithinByteLimit(full)
    }
}
