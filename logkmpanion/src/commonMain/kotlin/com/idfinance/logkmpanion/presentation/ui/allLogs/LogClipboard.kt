package com.idfinance.logkmpanion.presentation.ui.allLogs

internal const val CLIPBOARD_MAX_BYTES = 512 * 1024

internal data class ClipboardPayload(val text: String, val wasTruncated: Boolean)

internal fun tailWithinByteLimit(text: String, maxBytes: Int = CLIPBOARD_MAX_BYTES): ClipboardPayload {
    val bytes = text.encodeToByteArray()
    if (bytes.size <= maxBytes) return ClipboardPayload(text, false)

    // Skip UTF-8 continuation bytes (10xxxxxx) so we don't start mid-codepoint.
    var start = bytes.size - maxBytes
    while (start < bytes.size && (bytes[start].toInt() and 0xC0) == 0x80) start++

    val tail = bytes.copyOfRange(start, bytes.size).decodeToString()
    val firstNewline = tail.indexOf('\n')
    val result = if (firstNewline >= 0) tail.substring(firstNewline + 1) else tail
    return ClipboardPayload(result, true)
}
