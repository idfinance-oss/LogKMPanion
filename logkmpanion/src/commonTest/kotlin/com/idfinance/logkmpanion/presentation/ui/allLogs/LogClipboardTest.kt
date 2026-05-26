package com.idfinance.logkmpanion.presentation.ui.allLogs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LogClipboardTest {

    @Test
    fun emptyInput_returnsEmpty_notTruncated() {
        val result = tailWithinByteLimit("")
        assertEquals(ClipboardPayload("", false), result)
    }

    @Test
    fun underLimit_returnsOriginal_notTruncated() {
        val text = "hi"
        val result = tailWithinByteLimit(text, maxBytes = 100)
        assertEquals(ClipboardPayload(text, false), result)
    }

    @Test
    fun exactlyAtLimit_returnsOriginal_notTruncated() {
        val text = "a".repeat(100)
        val result = tailWithinByteLimit(text, maxBytes = 100)
        assertEquals(ClipboardPayload(text, false), result)
    }

    @Test
    fun overLimitWithNewline_dropsPartialFirstLine_marksTruncated() {
        val text = "line1\n" + "x".repeat(200)
        val result = tailWithinByteLimit(text, maxBytes = 100)
        assertTrue(result.wasTruncated)
        // first line ("line1") was beyond the cut and gets discarded
        assertFalse(result.text.startsWith("line1"))
        // result starts cleanly with the surviving content
        assertEquals("x".repeat(100), result.text)
    }

    @Test
    fun overLimitWithoutNewline_returnsTail_marksTruncated() {
        val text = "x".repeat(200)
        val result = tailWithinByteLimit(text, maxBytes = 100)
        assertTrue(result.wasTruncated)
        assertEquals(100, result.text.length)
        // pure ASCII tail, no replacement chars
        assertFalse(result.text.contains('�'))
    }

    @Test
    fun multibyteUtf8OnBoundary_skipsContinuationBytes_noReplacementChars() {
        // "ä" = 2 UTF-8 bytes (0xC3 0xA4). With maxBytes = 100, we want the cut
        // to land mid-codepoint so the continuation-byte skip logic kicks in.
        val text = "a".repeat(50) + "ä".repeat(50) + "tail"
        val result = tailWithinByteLimit(text, maxBytes = 100)
        assertTrue(result.wasTruncated)
        // continuation-byte skip ensures we never start with a replacement char
        assertFalse(result.text.contains('�'), "unexpected replacement char in: ${result.text}")
        // and the tail is preserved
        assertTrue(result.text.endsWith("tail"))
    }

    @Test
    fun multibyteUtf8WithNewline_dropsBrokenPrefix() {
        // Force a multibyte split, but include a newline so the partial-line trim runs too.
        val text = "ä".repeat(60) + "\nrest"
        val result = tailWithinByteLimit(text, maxBytes = 50)
        assertTrue(result.wasTruncated)
        assertEquals("rest", result.text)
    }
}
