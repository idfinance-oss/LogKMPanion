package com.idfinance.logkmpanion.presentation.ui.allLogs

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

internal class LogSharerImpl : LogSharer {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun shareAsFile(content: String, fileName: String) {
        val path = withContext(Dispatchers.Default) {
            val p = NSTemporaryDirectory() + fileName
            val bytes = content.encodeToByteArray()
            bytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
                    .writeToFile(p, atomically = true)
            }
            p
        }
        withContext(Dispatchers.Main) {
            val url = NSURL.fileURLWithPath(path)
            val vc = UIActivityViewController(activityItems = listOf(url), applicationActivities = null)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                vc, animated = true, completion = null,
            )
        }
    }
}
