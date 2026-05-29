package com.idfinance.logkmpanion.presentation.ui.allLogs

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class LogSharerImpl(private val context: Context) : LogSharer {
    override suspend fun shareAsFile(content: String, fileName: String) {
        val uri = withContext(Dispatchers.Default) {
            val file = File(context.cacheDir, fileName).apply { writeText(content) }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.logkmpanion.fileprovider",
                file,
            )
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(
            Intent.createChooser(intent, null).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    }
}
