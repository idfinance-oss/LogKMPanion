package com.idfinance.kmm.debug.view.presentation.ui.allLogs

import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debug.view.data.model.Log

internal interface AllLogsComponent {
    /**
     *
     */
    val model: Value<Model>

    /**
     *
     */
    fun clearLogs()

    data class Model(val logs: List<Log> = emptyList()) {
        val concatenatedLog: String
            get() = logs.joinToString("\n") { "[${it.tag}] it.message" }
    }
}