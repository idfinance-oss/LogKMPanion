package com.idfinance.logkmpanion.presentation.ui.allLogs

internal const val CLIPBOARD_MAX_BYTES = 512 * 1024

internal data class ClipboardPayload(val text: String, val wasTruncated: Boolean)

internal fun tailWithinByteLimit(text: String, maxBytes: Int = CLIPBOARD_MAX_BYTES): ClipboardPayload {
    val bytes = text.encodeToByteArray()
    if (bytes.size <= maxBytes) return ClipboardPayload(text, false)
    val tail = bytes.copyOfRange(bytes.size - maxBytes, bytes.size)
    val tailStr = tail.decodeToString(throwOnInvalidSequence = false)
    val cleanStart = tailStr.indexOf('\n').let { if (it >= 0) it + 1 else 0 }
    return ClipboardPayload(tailStr.substring(cleanStart), true)
}
