package com.idfinance.kmm.debug.view.presentation.ui.networkLogs.details

import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debug.view.presentation.ui.ModalComponent

internal interface NetworkLogDetailsComponent : ModalComponent {
    val model: Value<Model>

    data class Model(
        val method: String = "",
        val url: String = "",
        val statusCode: Int? = null,
        val duration: Long? = null,
        val request: Request? = null,
        val response: Response? = null,
        val error: String? = null
    ) {
        data class Request(
            val headers: List<String> = emptyList(),
            val cookies: List<String> = emptyList(),
            val body: String = ""
        )

        data class Response(
            val headers: List<String> = emptyList(),
            val body: String = "",
            val time: String = ""
        )
    }
}